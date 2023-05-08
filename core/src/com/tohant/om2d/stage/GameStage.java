package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.*;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.CacheService;
import com.tohant.om2d.storage.CacheProxy;

import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.utils.Align.left;
import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.ServiceUtil.checkHallNextToRoomThatHasNoOtherHalls;
import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class GameStage extends Stage {

    private Map map;
    private Label budget;
    private Array<TextButton> roomsButtons;
    private Room.Type currentRoom;
    private CacheProxy gameCache;
    private CacheService cacheService;
    private Label time;
    private Window toolPane;
    private Window officeStatWindow;
    private Label officeStatLabel;
    private Window roomInfo;
    private Label roomInfoLabel;
    private TextButton hideRoomInfo;
    private Skin skin;
    private Window notification;
    private Array<GameException> exceptions;
    private AsyncRoomBuildService roomBuildService;

    public GameStage(String time, Viewport viewport, Batch batch) {
        super(viewport, batch);
        Grid grid = new Grid((int) ((Gdx.graphics.getWidth() / 2f) - ((GRID_WIDTH * CELL_SIZE) / 2)),
                ((int) ((Gdx.graphics.getHeight() / 2f) - ((GRID_HEIGHT * CELL_SIZE) / 2))),
                GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
        map = new Map(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), grid);
        addActor(map);
        skin = getDefaultSkin();
        gameCache = new CacheProxy();
        cacheService = CacheService.getInstance();
        roomBuildService = AsyncRoomBuildService.getInstance();
        this.exceptions = new Array<>();
        this.budget = new Label("", skin);
        this.budget.setPosition(20, Gdx.graphics.getHeight() - 60);
        this.budget.setSize(100, 50);
        this.budget.setColor(Color.GREEN);
        setBudget(cacheService.getFloat(CURRENT_BUDGET));
        createToolPane();
        this.time = new Label(time, skin);
        this.time.setSize(200, 50);
        this.time.setPosition(Gdx.graphics.getWidth() - 20 - this.time.getWidth(), Gdx.graphics.getHeight() - 60);
        this.time.setColor(Color.BLACK);
        roomsButtons = new Array<>();
        Room.Type[] rooms = Room.Type.values();
        float buttonWidth = 200f;
        float buttonHeight = 60f;
        for (int i = 0, j = (int) toolPane.getHeight() + 40; i < rooms.length; i++, j += buttonHeight + 20) {
            TextButton room = new TextButton(rooms[i].name(), skin);
            room.setSize(buttonWidth, buttonHeight);
            room.setX(Gdx.graphics.getWidth() - (buttonWidth + 20));
            room.setY(j);
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
            addActor(room);
            roomsButtons.add(room);
        }
        addActor(this.budget);
        addActor(this.time);
        addActor(this.toolPane);
        this.roomInfoLabel = new Label("", skin);
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
                    if (a instanceof Dialog) {
                        if (a.getName().equals("user_info")) {
                            getActors().removeIndex(i);
                            break;
                        }
                    }
                }
                createNotification((GameException) e);
            } else {
                throw e;
            }
        } finally {
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
            builder.append(t.name());
            builder.append(": ");
            builder.append(getRoomsAmountByType(t));
            builder.append("\n");
        }
        builder.append("Incomes: ");
        builder.append(Math.round(cacheService.getFloat(TOTAL_INCOMES)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append("Costs: ");
        builder.append(Math.round(cacheService.getFloat(TOTAL_COSTS)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append("Salaries: ");
        builder.append(Math.round(cacheService.getFloat(TOTAL_SALARIES)));
        builder.append(" $/m");
        this.officeStatLabel.setText(builder.toString());
        this.officeStatWindow.setSize(this.officeStatWindow.getPrefWidth(), this.officeStatWindow.getPrefHeight());
    }

    public void updateRoomInfoWindow() {
        Cell currentCell = null;
        String currentId = cacheService.getValue(CURRENT_ROOM);
        for (Actor a : map.getGrid().getChildren().items) {
            if (a instanceof Cell) {
                if (((Cell) a).getRoomModel() != null
                        && ((Cell) a).getRoomModel().getRoomInfo().getId().equals(currentId)) {
                    currentCell = ((Cell) a);
                }
            }
        }
        if (currentCell != null) {
            if (currentCell.getRoomModel() != null) {
                RoomInfo currentRoomInfo = currentCell.getRoomModel().getRoomInfo();
                Staff.Type currentStaffType = null;
                float currentStaffTypeSalary = 0.0f;
                switch (currentRoomInfo.getType()) {
                    case SECURITY:
                        currentStaffType = Staff.Type.SECURITY;
                        currentStaffTypeSalary = 1200.0f;
                        break;
                    case OFFICE:
                        currentStaffType = Staff.Type.WORKER;
                        break;
                    case CLEANING:
                        currentStaffType = Staff.Type.CLEANING;
                        currentStaffTypeSalary = 500.0f;
                        break;
                }
                String name = currentRoomInfo.getType().name().charAt(0) +
                        currentRoomInfo.getType().name().substring(1).toLowerCase();
                roomInfo.reset();
                if (!currentCell.isBuilt() && !currentCell.isEmpty()) {
                    roomInfo.getTitleLabel().setText("Construction");
                    this.roomInfoLabel.setText("Building " + currentCell.getRoomModel().getRoomInfo().getType()
                            .name().toLowerCase() + " room...");
                } else {
                    roomInfo.getTitleLabel().setText(name + " #" + currentRoomInfo.getNumber());
                    this.roomInfoLabel.setText("Price: " + Math.round(currentRoomInfo.getPrice()) + "$\n"
                            + "Cost: " + Math.round(currentRoomInfo.getCost()) + "$/m\n" + "Employees: "
                            + currentRoomInfo.getStaff().size);
                }
                TextButton destroy = new TextButton("Destroy", skin);
                Cell currentCellCopy = currentCell;
                Staff.Type currentStaffTypeCopy = currentStaffType;
                float currentStaffTypeSalaryCopy = currentStaffTypeSalary;
                destroy.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        if (checkHallNextToRoomThatHasNoOtherHalls(currentCellCopy, map.getGrid().getChildren())
                                && currentCellCopy.getRoomModel().getRoomInfo().getType() == Room.Type.HALL) {
                            addException(new GameException(GameException.Code.E300));
                        } else {
                            cacheService.setFloat(TOTAL_COSTS, cacheService.getFloat(TOTAL_COSTS) - currentCellCopy.getRoomModel().getRoomInfo().getCost());
                            if (currentRoomInfo.getType() == Room.Type.OFFICE) {
                                cacheService.setFloat(TOTAL_INCOMES, cacheService.getFloat(TOTAL_INCOMES) - 1000.0f);
                            }
                            if (currentStaffTypeCopy != null) {
                                setEmployeesAmountByType(currentStaffTypeCopy,
                                        getEmployeesAmountByType(currentStaffTypeCopy) - currentRoomInfo.getStaff().size);
                            }
                            gameCache.setValue(TOTAL_SALARIES, Float.parseFloat((String) gameCache.getValue(TOTAL_SALARIES))
                                    - currentRoomInfo.getStaff().size * currentStaffTypeSalaryCopy);
                            setRoomsAmountByType(currentCellCopy.getRoomModel().getRoomInfo().getType(),
                                    getRoomsAmountByType(currentCellCopy.getRoomModel().getRoomInfo().getType()) - 1L);
                            gameCache.setValue(CURRENT_ROOM, null);
                            currentCellCopy.setRoomModel(null);
                            roomInfo.setVisible(false);
                            return true;
                        }
                        return false;
                    }
                });
                roomInfo.add(this.roomInfoLabel).expand().pad(20).center();
                roomInfo.row();
                roomInfo.add(destroy).expand().padLeft(20).padRight(20).padBottom(20).center();
                roomInfo.setSize(MathUtils.clamp(roomInfo.getWidth(), roomInfo.getPrefWidth() + 50, roomInfo.getPrefWidth() + 200), roomInfo.getPrefHeight());
                roomInfo.setVisible(true);
            }
        }
    }

    private void createToolPane() {
        this.officeStatLabel = new Label("", skin);
        this.officeStatWindow = new Window("Office", skin);
        TextButton hideOfficeStatWindow = new TextButton("x", skin);
        hideOfficeStatWindow.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                officeStatWindow.setVisible(false);
                return false;
            }
        });
        this.officeStatWindow.setMovable(true);
        this.officeStatWindow.setResizable(false);
        setRoomsStat();
        this.officeStatWindow.add(this.officeStatLabel).center().pad(20).expand();
        this.officeStatWindow.getTitleTable().add(hideOfficeStatWindow).right();
        this.officeStatWindow.setVisible(false);
        this.officeStatWindow.setSize(this.officeStatWindow.getPrefWidth(), this.officeStatWindow.getPrefHeight());
        this.officeStatWindow.setPosition(this.budget.getX(), this.budget.getY() - 20 - this.officeStatWindow.getHeight());
        this.roomInfo = new Window("Room Info", skin);
        this.hideRoomInfo = new TextButton("x", skin);
        hideRoomInfo.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                roomInfo.setVisible(false);
                return false;
            }
        });
        this.roomInfo.setMovable(true);
        this.roomInfo.setResizable(false);
        this.roomInfo.getTitleTable().add(hideRoomInfo).right();
        this.roomInfo.setVisible(false);
        this.roomInfo.setSize(this.roomInfo.getPrefWidth() + hideRoomInfo.getWidth(), this.roomInfo.getPrefHeight());
        this.roomInfo.setPosition(Gdx.graphics.getWidth() - this.roomInfo.getWidth() - 20, this.budget.getY() - 20 - this.roomInfo.getHeight());
        this.toolPane = new Window("Office Manager 2D", skin);
        this.toolPane.setSize(Gdx.graphics.getWidth(), 150f);
        this.toolPane.setPosition(0, 0);
        this.toolPane.setMovable(false);
        this.toolPane.setResizable(false);
        this.toolPane.align(left);
        TextButton hide = new TextButton("-", skin);
        hide.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                toolPane.setHeight(toolPane.getTouchable() == Touchable.enabled ? 50f : 150f);
                toolPane.setTouchable(toolPane.getTouchable() == Touchable.enabled
                        ? Touchable.childrenOnly : Touchable.enabled);
                return false;
            }
        });
        TextButton officeButton = new TextButton("Office", skin);
        officeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                officeStatWindow.setVisible(!officeStatWindow.isVisible());
                return false;
            }
        });
        addActor(this.officeStatWindow);
        addActor(this.roomInfo);
        this.toolPane.add(officeButton);
        this.toolPane.getTitleTable().add(hide).right();
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void dispose() {
        skin.dispose();
    }

    private long getRoomsAmountByType(Room.Type type) {
        switch (type) {
            case OFFICE:
                return cacheService.getLong(OFFICES_AMOUNT);
            case HALL:
                return cacheService.getLong(HALLS_AMOUNT);
            case SECURITY:
                return cacheService.getLong(SECURITY_AMOUNT);
            case CLEANING:
                return cacheService.getLong(CLEANING_AMOUNT);
            default:
                return -1L;
        }
    }

    private void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE:
                cacheService.setLong(OFFICES_AMOUNT, amount);
                break;
            case HALL:
                cacheService.setLong(HALLS_AMOUNT, amount);
                break;
            case SECURITY:
                cacheService.setLong(SECURITY_AMOUNT, amount);
                break;
            case CLEANING:
                cacheService.setLong(CLEANING_AMOUNT, amount);
                break;
            default:
                break;
        }
    }

    private long getEmployeesAmountByType(Staff.Type type) {
        switch (type) {
            case SECURITY:
                return Long.parseLong((String) gameCache.getValue(TOTAL_SECURITY_STAFF));
            case WORKER:
                return Long.parseLong((String) gameCache.getValue(TOTAL_WORKERS));
            case CLEANING:
                return Long.parseLong((String) gameCache.getValue(TOTAL_CLEANING_STAFF));
            case ADMINISTRATION:
                return Long.parseLong((String) gameCache.getValue(TOTAL_ADMIN_STAFF));
            default:
                return -1L;
        }
    }

    private void setEmployeesAmountByType(Staff.Type type, long amount) {
        switch (type) {
            case SECURITY:
                gameCache.setValue(TOTAL_SECURITY_STAFF, amount);
                break;
            case CLEANING:
                gameCache.setValue(TOTAL_CLEANING_STAFF, amount);
                break;
            case WORKER:
                gameCache.setValue(TOTAL_WORKERS, amount);
                break;
            case ADMINISTRATION:
                gameCache.setValue(TOTAL_ADMIN_STAFF, amount);
                break;
            default:
                break;
        }
    }

    private void createNotification(GameException e) {
        this.notification = new Window("", skin);
        this.notification.setMovable(false);
        this.notification.setResizable(false);
        this.notification.setName("user_info");
        this.notification.addAction(sequence(delay(4f), fadeOut(3f)));
        this.notification.getTitleLabel().setText(e.getCode().getType().getTitle());
        this.notification.add(new Label(e.getCode().getMessage(), skin)).center().expand();
        this.notification.setSize(this.notification.getPrefWidth(), this.notification.getPrefHeight());
        this.notification.setPosition(Gdx.graphics.getWidth() / 2f
                - this.notification.getWidth() / 2f, Gdx.graphics.getHeight() - this.notification.getHeight() - 20);
        addActor(this.notification);
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
        if (this.roomInfoLabel != null) {
            String id = cacheService.getValue(CURRENT_ROOM);
            if (id != null) {
                AtomicReference<TimeLineTask<Room>> roomBuildingTimeline = new AtomicReference<>();
                roomBuildService.getTasks().forEach(t -> {
                    if (t.getId().equals(id)) {
                        roomBuildingTimeline.set(t);
                    }
                });
                if (roomBuildingTimeline.get() != null) {
                    map.getGrid().getChildren().forEach(c -> {
                        if (!((Cell) c).isEmpty() && !((Cell) c).isBuilt()
                                && ((Cell) c).getRoomModel().getRoomInfo().getId().equals(id)) {
                            long days = roomBuildingTimeline.get().getDate().getDays();
                            days = ((Cell) c).getRoomModel().getRoomInfo().getBuildTime().getDays() - days;
                            long months = roomBuildingTimeline.get().getDate().getMonth();
                            months = months == 1L ? 0 : ((Cell) c).getRoomModel().getRoomInfo().getBuildTime().getMonth() - months;
                            long years = roomBuildingTimeline.get().getDate().getYears();
                            years = years == 1L ? 0 : ((Cell) c).getRoomModel().getRoomInfo().getBuildTime().getYears() - years;
                            this.roomInfoLabel.setText("Building " + ((Cell) c).getRoomModel().getRoomInfo().getType()
                                    .name().toLowerCase() + " room...\nTime left: " + days + " d. " + months + " m." + years + " y.");
                        }
                    });
                } else {
                    updateRoomInfoWindow();
                }
            }
        }
    }

}
