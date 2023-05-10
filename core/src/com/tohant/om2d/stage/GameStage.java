package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.*;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.dropdown.HorizontalDropdown;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.actor.ui.modal.IModal;
import com.tohant.om2d.actor.ui.modal.ModalData;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.CacheService;
import com.tohant.om2d.storage.CacheProxy;

import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.utils.Align.left;
import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class GameStage extends Stage {

    private static final String USER_INFO = "USER_INFO";

    private Map map;
    private Label budget;
    private Array<TextButton> roomsButtons;
    private Room.Type currentRoom;
    private CacheProxy gameCache;
    private CacheService cacheService;
    private Label time;
    private Window toolPane;
    private IModal officeStatModal;
    private IModal roomInfoModal;
    private Skin skin;
    private IModal notification;
    private Array<GameException> exceptions;
    private AsyncRoomBuildService roomBuildService;
    private HorizontalDropdown roomsMenu;
    private TextButton destroy;
    private float deltaTimestamp;

    public GameStage(String time, Viewport viewport, Batch batch) {
        super(viewport, batch);
        map = new Map(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addActor(map);
        skin = getDefaultSkin();
        gameCache = new CacheProxy();
        cacheService = CacheService.getInstance();
        roomBuildService = AsyncRoomBuildService.getInstance();
        this.exceptions = new Array<>();
        this.budget = new Label("", skin);
        this.budget.setPosition(DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3);
        this.budget.setFontScale(1.5f);
        this.budget.setSize(100 * this.budget.getFontScaleX(), 50);
        this.budget.setColor(Color.GREEN);
        setBudget(cacheService.getFloat(CURRENT_BUDGET));
        this.time = new Label(time, skin);
        this.time.setFontScale(1.5f);
        this.time.setSize(100 * this.time.getFontScaleX(), 50);
        this.time.setPosition(Gdx.graphics.getWidth() - this.time.getFontScaleX() * this.time.getWidth() - DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3);
        this.time.setColor(Color.BLACK);
        roomsButtons = new Array<>();
        Room.Type[] rooms = Room.Type.values();
        for (int i = 0; i < rooms.length; i++) {
            TextButton room = new TextButton(rooms[i].name(), skin);
            int iCopy = i;
            room.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchDown(event, x, y, pointer, button);
                    currentRoom = rooms[iCopy];
                    cacheService.setValue(CURRENT_ROOM_TYPE, currentRoom.name());
                    return false;
                }
            });
            roomsButtons.add(room);
        }
        createDestroyButton();
        createToolPane();
        this.roomsMenu = new HorizontalDropdown(Gdx.graphics.getWidth() - DEFAULT_PAD * 6.5f,
                toolPane.getHeight() + DEFAULT_PAD * 2 * roomsButtons.size, roomsButtons, skin);
        addActor(this.toolPane);
        addActor(this.roomsMenu);
        addActor(this.budget);
        addActor(this.time);
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);
            checkForExceptionsAndThrowIfExist(0);
        } catch (Exception e) {
            if (e instanceof GameException) {
                for (int i = 0; i < getActors().size; i++) {
                    Actor a = getActors().get(i);
                    if (a instanceof Window) {
                        if (a.getName() != null) {
                            if (a.getName().equals(USER_INFO)) {
                                getActors().removeIndex(i);
                                break;
                            }
                        }
                    }
                }
                createNotification((GameException) e);
            } else {
                throw e;
            }
        } finally {
            if (deltaTimestamp * 1000L >= DAY_WAIT_TIME_MILLIS) {
                deltaTimestamp = 0.0f;
            } else {
                deltaTimestamp += Gdx.graphics.getDeltaTime();
            }
            setBudget(cacheService.getFloat(CURRENT_BUDGET));
            setRoomsStat();
            updateRoomInfoLabel();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean childHandled = false;
        Actor actor = hit(screenX, screenY, false);
        if (actor != null) {
            try {
                childHandled = actor.fire(new InputEvent());
            } catch (RuntimeException ignored) {

            }
        }
        boolean superTouchDown = false;
        try {
            superTouchDown = super.touchDown(screenX, screenY, pointer, button);
        } catch (RuntimeException ignored) {

        }
        return childHandled || superTouchDown;
    }

    public Label getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        if (budget < 0) {
            this.budget.setColor(Color.RED);
        } else {
            this.budget.setColor(Color.GREEN);
        }
        this.budget.setText(Math.round(budget) + " $");
    }

    public Room.Type getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room.Type currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Label getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time.setText(time);
        if (this.time.getX() + this.time.getWidth() >= Gdx.graphics.getWidth()) {
            this.time.setSize(this.time.getWidth() + 20, this.time.getHeight());
        }
    }

    private void setRoomsStat() {
        StringBuilder builder = new StringBuilder();
        builder.append("Rooms:\n");
        for (Room.Type t : Room.Type.values()) {
            builder.append(" ");
            builder.append(t.name().charAt(0));
            builder.append(t.name().substring(1).toLowerCase());
            builder.append(": ");
            builder.append(getRoomsAmountByType(t));
            builder.append("\n");
        }
        builder.append("Finances:\n");
        builder.append(" Incomes: ");
        builder.append(Math.round(cacheService.getFloat(TOTAL_INCOMES)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append(" Costs: ");
        builder.append(Math.round(cacheService.getFloat(TOTAL_COSTS)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append(" Salaries: ");
        builder.append(Math.round(cacheService.getFloat(TOTAL_SALARIES)));
        builder.append(" $/m");
        this.officeStatModal.updateContentText(builder.toString());
//        this.officeStatLabel.setText(builder.toString());
//        this.officeStatWindow.setSize(this.officeStatWindow.getPrefWidth(), this.officeStatWindow.getPrefHeight());
    }

    private ModalData updateRoomInfoWindow(Cell currentCell) {
//        Cell currentCell = null;
//        String currentId = cacheService.getValue(CURRENT_ROOM);
//        for (Actor a : map.getGrid().getChildren().items) {
//            if (a instanceof Cell) {
//                if (((Cell) a).getRoomModel() != null
//                        && ((Cell) a).getRoomModel().getRoomInfo().getId().equals(currentId)) {
//                    currentCell = ((Cell) a);
//                }
//            }
//        }
//        if (currentCell != null) {
//            if (currentCell.getRoomModel() != null) {
//                RoomInfo currentRoomInfo = currentCell.getRoomModel().getRoomInfo();
//                Staff.Type currentStaffType = null;
//                float currentStaffTypeSalary = 0.0f;
//                switch (currentRoomInfo.getType()) {
//                    case SECURITY:
//                        currentStaffType = Staff.Type.SECURITY;
//                        currentStaffTypeSalary = Staff.Type.SECURITY.getSalary();
//                        break;
//                    case OFFICE:
//                        currentStaffType = Staff.Type.WORKER;
//                        break;
//                    case CLEANING:
//                        currentStaffType = Staff.Type.CLEANING;
//                        currentStaffTypeSalary = Staff.Type.CLEANING.getSalary();
//                        break;
//                }
        String title = currentCell.getRoomModel().getRoomInfo().getType().name().charAt(0) +
                currentCell.getRoomModel().getRoomInfo().getType().name().substring(1).toLowerCase()
                + " #" + currentCell.getRoomModel().getRoomInfo().getNumber();
        String text = "Price: " + Math.round(currentCell.getRoomModel().getRoomInfo().getPrice()) + "$\n"
                + "Cost: " + Math.round(currentCell.getRoomModel().getRoomInfo().getCost()) + "$/m\n" + "Employees: "
                + currentCell.getRoomModel().getRoomInfo().getStaff().size;
        return new ModalData(title, text);
//                TextButton destroy = new TextButton("Destroy", skin);
//                Cell currentCellCopy = currentCell;
//                Staff.Type currentStaffTypeCopy = currentStaffType;
//                float currentStaffTypeSalaryCopy = currentStaffTypeSalary;
//                destroy.addListener(new InputListener() {
//                    @Override
//                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                        super.touchDown(event, x, y, pointer, button);
//                        if (checkHallNextToRoomThatHasNoOtherHalls(currentCellCopy, map.getGrid().getChildren())
//                                && currentCellCopy.getRoomModel().getRoomInfo().getType() == Room.Type.HALL) {
//                            addException(new GameException(GameException.Code.E300));
//                        } else {
//                            cacheService.setFloat(TOTAL_COSTS, cacheService.getFloat(TOTAL_COSTS) - currentCellCopy.getRoomModel().getRoomInfo().getCost());
//                            if (currentRoomInfo.getType() == Room.Type.OFFICE) {
//                                cacheService.setFloat(TOTAL_INCOMES, cacheService.getFloat(TOTAL_INCOMES) - 100.0f * currentRoomInfo.getStaff().size);
//                            }
//                            if (currentStaffTypeCopy != null) {
//                                setEmployeesAmountByType(currentStaffTypeCopy,
//                                        getEmployeesAmountByType(currentStaffTypeCopy) - currentRoomInfo.getStaff().size);
//                            }
//                            gameCache.setValue(TOTAL_SALARIES, Float.parseFloat((String) gameCache.getValue(TOTAL_SALARIES))
//                                    - currentRoomInfo.getStaff().size * currentStaffTypeSalaryCopy);
//                            setRoomsAmountByType(currentCellCopy.getRoomModel().getRoomInfo().getType(),
//                                    getRoomsAmountByType(currentCellCopy.getRoomModel().getRoomInfo().getType()) - 1L);
//                            gameCache.setValue(CURRENT_ROOM, null);
//                            currentCellCopy.setRoomModel(null);
//                            roomInfoModal.setVisible(false);
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//                roomInfoModal.add(this.roomInfoLabel).expand().pad(20).center();
//                roomInfoModal.row();
//                roomInfoModal.add(destroy).expand().padLeft(20).padRight(20).padBottom(20).center();
//                roomInfoModal.setSize(MathUtils.clamp(roomInfoModal.getWidth(), roomInfoModal.getPrefWidth() + 50, roomInfoModal.getPrefWidth() + 200), roomInfoModal.getPrefHeight());
//            roomInfoModal.getThis().setVisible(true);
//        }
    }

    private void createToolPane() {
//        this.officeStatLabel = new Label("", skin);
//        this.officeStatWindow = new Window("Office", skin);
        this.officeStatModal = new DefaultModal(new ModalData("Statistics", "", Array.with(),
                new Vector2(this.budget.getX(), this.budget.getY() - DEFAULT_PAD)));
//        TextButton hideOfficeStatWindow = new TextButton("x", skin);
//        hideOfficeStatWindow.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchDown(event, x, y, pointer, button);
//                officeStatModal.setVisible(false);
//                return false;
//            }
//        });
//        this.officeStatModal.setMovable(true);
//        this.officeStatModal.setResizable(false);
        setRoomsStat();
//        this.officeStatModal.add(this.officeStatLabel).center().pad(20).expand();
//        this.officeStatModal.getTitleTable().add(hideOfficeStatWindow).right();
//        this.officeStatModal.setVisible(false);
//        this.officeStatModal.setSize(this.officeStatModal.getPrefWidth(), this.officeStatModal.getPrefHeight());
        this.roomInfoModal = new DefaultModal(new ModalData("Room Info", "", Array.with(this.destroy),
                new Vector2(Gdx.graphics.getWidth() - DEFAULT_PAD, this.budget.getY() - DEFAULT_PAD)));
//        this.roomInfoModal = new Window("Room Info", skin);
//        this.hideRoomInfo = new TextButton("x", skin);
//        hideRoomInfo.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchDown(event, x, y, pointer, button);
//                roomInfoModal.setVisible(false);
//                return false;
//            }
//        });
//        this.roomInfoModal.setMovable(true);
//        this.roomInfoModal.setResizable(false);
//        this.roomInfoModal.getTitleTable().add(hideRoomInfo).right();
//        this.roomInfoModal.setVisible(false);
//        this.roomInfoModal.setSize(this.roomInfoModal.getPrefWidth() + hideRoomInfo.getWidth(), this.roomInfoModal.getPrefHeight());
//        this.roomInfoModal.setPosition(Gdx.graphics.getWidth() - this.roomInfoModal.getWidth() - 20, this.budget.getY() - 20 - this.roomInfoModal.getHeight());
        this.toolPane = new Window("Office Manager 2D", skin);
        this.toolPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 7f);
        this.toolPane.setPosition(0, 0);
        this.toolPane.setMovable(false);
        this.toolPane.setResizable(false);
        this.toolPane.align(left);
        TextButton officeButton = new TextButton("Office", skin);
        officeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                officeStatModal.toggle();
                return false;
            }
        });
        TextButton hide = new TextButton("-", skin);
        hide.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                if (toolPane.getTouchable() == Touchable.enabled) {
                    toolPane.setHeight(toolPane.getTitleTable().getHeight() + DEFAULT_PAD);
                    hide.setText("+");
                } else {
                    toolPane.setHeight(Gdx.graphics.getHeight() / 7f);
                    hide.setText("-");
                }
//                toolPane.setHeight(toolPane.getTouchable() == Touchable.enabled ? 50f : 150f);
                toolPane.setTouchable(toolPane.getTouchable() == Touchable.enabled
                        ? Touchable.childrenOnly : Touchable.enabled);
                return false;
            }
        });

        addActor(this.officeStatModal.getThis());
        addActor(this.roomInfoModal.getThis());
        this.toolPane.add(officeButton).padLeft(DEFAULT_PAD).padBottom(DEFAULT_PAD);
        this.toolPane.getTitleTable().getCells().get(0).pad(DEFAULT_PAD);
        this.toolPane.getTitleTable().add(hide).right();
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void dispose() {
        skin.dispose();
    }

    private void createNotification(GameException e) {
        this.notification = new DefaultModal(new ModalData(e.getCode().getType().getTitle(), e.getCode().getMessage(), Array.with()));
        this.notification.getThis().setVisible(true);
        this.notification.getThis().setMovable(false);
        this.notification.getThis().setName(USER_INFO);
        this.notification.getThis().addAction(sequence(delay(4f), fadeOut(3f)));
        this.notification.getThis().setSize(this.notification.getThis().getPrefWidth(), this.notification.getThis().getPrefHeight());
        this.notification.getThis().setPosition(Gdx.graphics.getWidth() / 2f
                - this.notification.getThis().getWidth() / 2f, Gdx.graphics.getHeight() - this.notification.getThis().getHeight() - DEFAULT_PAD);
        addActor(this.notification.getThis());
    }

    private void checkForExceptionsAndThrowIfExist(int i) {
        if (this.exceptions.size > 0 && i < this.exceptions.size) {
            checkForExceptionsAndThrowIfExist(i + 1);
            GameException e = this.exceptions.get(i);
            this.exceptions.removeIndex(i);
            throw e;
        }
    }

    public void addException(GameException e) {
        this.exceptions.add(e);
    }

    private void updateRoomInfoLabel() {
        if (this.roomInfoModal != null) {
                Cell currentCell = null;
                String currentId = cacheService.getValue(CURRENT_ROOM);
                System.out.println(currentId);
                if (currentId != null) {
                    for (Actor a : map.getGrid().getChildren().items) {
                        if (a instanceof Cell) {
                            if (((Cell) a).getRoomModel() != null
                                    && ((Cell) a).getRoomModel().getRoomInfo().getId().equals(currentId)) {
                                currentCell = ((Cell) a);
                            }
                        }
                    }
                    if (currentCell != null) {
                        ModalData modalData;
                        AtomicReference<TimeLineTask<Room>> roomBuildingTimeline = new AtomicReference<>();
                        roomBuildService.getTasks().forEach(t -> {
                            if (t.getId().equals(currentId)) {
                                roomBuildingTimeline.set(t);
                            }
                        });
                        if (roomBuildingTimeline.get() != null && !roomBuildingTimeline.get().isFinished()) {
                            long days = roomBuildingTimeline.get().getDate().getDays();
                            days = currentCell.getRoomModel().getRoomInfo().getBuildTime().getDays() - days;
                            long months = roomBuildingTimeline.get().getDate().getMonth();
                            months = months == 1L ? 0 : currentCell.getRoomModel().getRoomInfo().getBuildTime().getMonth() - months;
                            long years = roomBuildingTimeline.get().getDate().getYears();
                            years = years == 1L ? 0 : currentCell.getRoomModel().getRoomInfo().getBuildTime().getYears() - years;
                            String title = "Construction " + buildSymbol();
                            String text = "Building " + currentCell.getRoomModel().getRoomInfo().getType()
                                    .name().toLowerCase() + " room\n\nTime left: " + days + " d. " + months + " m. " + years + " y.";
                            modalData = new ModalData(title, text);
                        } else {
                            modalData = updateRoomInfoWindow(currentCell);
                        }
                        this.roomInfoModal.getThis().getTitleLabel().setText(modalData.getTitle());
                        this.roomInfoModal.updateContentText(modalData.getText());
                    }
                }
            }
//            } else {
//                this.map.getGrid().getChildren().forEach(c -> {
//                    if (c instanceof Cell) {
//                        if (((Cell) c).isEmpty() && ((Cell) c).getRoomModel() != null) {
//                            ((Cell) c).setRoomModel(null);
//                        }
//                    }
//                });
//            }
    }

    private String buildSymbol() {
        float deltaTime = 87.5f;
        float deltaTimeStampMillis = deltaTimestamp * 1000f;
        if (deltaTimeStampMillis <= deltaTime) {
            return "|";
        } else if (deltaTimeStampMillis <= deltaTime * 2f) {
            return "/";
        } else if (deltaTimeStampMillis <= deltaTime * 3f) {
            return "-";
        } else if (deltaTimeStampMillis <= deltaTime * 4f) {
            return "\\";
        } else if (deltaTimeStampMillis <= deltaTime * 5f) {
            return "|";
        } else if (deltaTimeStampMillis <= deltaTime * 6f) {
            return "/";
        } else if (deltaTimeStampMillis <= deltaTime * 7f) {
            return "-";
        } else if (deltaTimeStampMillis <= deltaTime * 8f) {
            return "\\";
        } else {
            return "|";
        }
    }

    private void createDestroyButton() {
        this.destroy = new TextButton("Destroy", skin);
        this.destroy.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                AtomicReference<Cell> currentCell = new AtomicReference<>();
                String id = cacheService.getValue(CURRENT_ROOM);
                if (id != null) {
                    getMap().getGrid().getChildren().forEach(c -> {
                        if (c instanceof Cell) {
                            if (((Cell) c).getRoomModel() != null) {
                                if (((Cell) c).getRoomModel().getRoomInfo().getId().equals(id)) {
                                    currentCell.set((Cell) c);
                                }
                            }
                        }
                    });
                    if (currentCell.get() != null && !currentCell.get().isEmpty()) {
                        if (checkHallNextToRoomThatHasNoOtherHalls(currentCell.get(), map.getGrid().getChildren())
                                && currentCell.get().getRoomModel().getRoomInfo().getType() == Room.Type.HALL) {
                            addException(new GameException(GameException.Code.E300));
                        } else {
                            Staff.Type currentStaffType = null;
                            float currentStaffTypeSalary = 0.0f;
                            switch (currentCell.get().getRoomModel().getRoomInfo().getType()) {
                                case SECURITY:
                                    currentStaffType = Staff.Type.SECURITY;
                                    currentStaffTypeSalary = Staff.Type.SECURITY.getSalary();
                                    break;
                                case OFFICE:
                                    currentStaffType = Staff.Type.WORKER;
                                    break;
                                case CLEANING:
                                    currentStaffType = Staff.Type.CLEANING;
                                    currentStaffTypeSalary = Staff.Type.CLEANING.getSalary();
                                    break;
                            }
                            cacheService.setFloat(TOTAL_COSTS, cacheService.getFloat(TOTAL_COSTS) - currentCell.get().getRoomModel().getRoomInfo().getCost());
                            if (currentCell.get().getRoomModel().getRoomInfo().getType() == Room.Type.OFFICE
                            && currentCell.get().getRoomModel().getRoom().isDone()) {
                                cacheService.setFloat(TOTAL_INCOMES, cacheService.getFloat(TOTAL_INCOMES) - 100.0f
                                        * currentCell.get().getRoomModel().getRoomInfo().getStaff().size);
                            }
                            if (currentStaffType != null) {
                                setEmployeesAmountByType(currentStaffType,
                                        getEmployeesAmountByType(currentStaffType)
                                                - currentCell.get().getRoomModel().getRoomInfo().getStaff().size);
                            }
                            cacheService.setFloat(TOTAL_SALARIES, cacheService.getFloat(TOTAL_SALARIES)
                                    - currentCell.get().getRoomModel().getRoomInfo().getStaff().size * currentStaffTypeSalary);
                            setRoomsAmountByType(currentCell.get().getRoomModel().getRoomInfo().getType(),
                                    getRoomsAmountByType(currentCell.get().getRoomModel().getRoomInfo().getType()) - 1L);
                            cacheService.setValue(CURRENT_ROOM, null);
                            currentCell.get().setRoomModel(null);
                            roomInfoModal.getThis().setVisible(false);
                            return false;
                        }
                    } else {
                        destroy.setDisabled(true);
                    }
                }
                return false;
            }
        });
    }

    public IModal getRoomInfoModal() {
        return roomInfoModal;
    }

}
