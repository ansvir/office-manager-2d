package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.*;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.storage.CacheProxy;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.utils.Align.left;
import static com.tohant.om2d.service.ServiceUtil.checkHallNextToRoomThatHasNoOtherHalls;
import static com.tohant.om2d.service.ServiceUtil.nextToHalls;
import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class GameStage extends Stage {

    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private static final int CELL_SIZE = 80;

    private Map map;
    private Label budget;
    private Array<TextButton> roomsButtons;
    private Room.Type currentRoom;
    private CacheProxy gameCache;
    private Label time;
    private Window toolPane;
    private Window officeStatWindow;
    private Label officeStatLabel;
    private Window roomInfo;
    private Skin skin;
    private Window notification;
    private Array<GameException> exceptions;

    public GameStage(float budget, String time, Viewport viewport, Batch batch) {
        super(viewport, batch);
        Grid grid = new Grid((int) ((Gdx.graphics.getWidth() / 2f) - ((GRID_WIDTH * CELL_SIZE) / 2)),
                ((int) ((Gdx.graphics.getHeight() / 2f) - ((GRID_HEIGHT * CELL_SIZE)/ 2))),
                GRID_WIDTH, GRID_HEIGHT,  CELL_SIZE);
        map = new Map(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), grid);
        addActor(map);
        skin = getDefaultSkin();
        gameCache = new CacheProxy();
        this.exceptions = new Array<>();
        this.budget = new Label(Math.round(budget) + " $", skin);
        this.budget.setPosition(20, Gdx.graphics.getHeight() - 60);
        this.budget.setSize(100, 50);
        this.budget.setColor(Color.GREEN);
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
                    gameCache.setValue(CURRENT_ROOM_TYPE, currentRoom.name());
                    return false;
                }
            });
            addActor(room);
            roomsButtons.add(room);
        }
        addActor(this.budget);
        addActor(this.time);
        addActor(this.toolPane);
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
            setBudget(Float.parseFloat((String) gameCache.getValue(CURRENT_BUDGET)));
            setRoomsStat();
            updateRoomInfoWindow();
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
        builder.append(Math.round(Float.parseFloat((String) gameCache.getValue(TOTAL_INCOMES))));
        builder.append(" $/m");
        builder.append("\n");
        builder.append("Costs: ");
        builder.append(Math.round(Float.parseFloat((String) gameCache.getValue(TOTAL_COSTS))));
        builder.append(" $/m");
        builder.append("\n");
        builder.append("Salaries: ");
        builder.append(Math.round(Float.parseFloat((String) gameCache.getValue(TOTAL_SALARIES))));
        builder.append(" $/m");
        this.officeStatLabel.setText(builder.toString());
        this.officeStatWindow.setSize(this.officeStatWindow.getPrefWidth(), this.officeStatWindow.getPrefHeight());
    }

    private void updateRoomInfoWindow() {
        Cell currentCell = null;
        String id = (String) gameCache.getValue(CURRENT_ROOM);
        for (Actor a : map.getGrid().getChildren().items) {
            if (a instanceof Cell) {
                if (((Cell) a).getRoom() != null &&
                        ((Cell) a).getRoom().getId().equals(id)) {
                    currentCell = ((Cell) a);
                }
            }
        }
        if (currentCell != null) {
            if (currentCell.getRoom() != null) {
                Room currentRoom = currentCell.getRoom();
                Staff.Type currentStaffType = null;
                float currentStaffTypeSalary = 0.0f;
                switch (currentRoom.getType()) {
                    case SECURITY: currentStaffType = Staff.Type.SECURITY; currentStaffTypeSalary = 1200.0f; break;
                    case OFFICE: currentStaffType = Staff.Type.WORKER; break;
                    case CLEANING: currentStaffType = Staff.Type.CLEANING; currentStaffTypeSalary = 500.0f; break;
                }
                String name = currentRoom.getType().name().charAt(0) +
                        currentRoom.getType().name().substring(1).toLowerCase();
                roomInfo.getTitleLabel().setText(name + " #" + currentRoom.getNumber());
                roomInfo.clearChildren();
                Label roomInfoLabel = new Label("Price: " + Math.round(currentRoom.getPrice()) + "$\n"
                        + "Cost: " + Math.round(currentRoom.getCost()) + "$/m\n" + "Employees: "
                        + currentRoom.getStaff().size, skin);
                TextButton destroy = new TextButton("Destroy", skin);
                Cell currentCellCopy = currentCell;
                Staff.Type currentStaffTypeCopy = currentStaffType;
                float currentStaffTypeSalaryCopy = currentStaffTypeSalary;
                destroy.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        if (checkHallNextToRoomThatHasNoOtherHalls(currentCellCopy, map.getGrid().getChildren())
                                && currentCellCopy.getRoom().getType() == Room.Type.HALL) {
                            addException(new GameException(GameException.Code.E300));
                        } else {
                            gameCache.setValue(TOTAL_COSTS, Float.parseFloat((String) gameCache.getValue(TOTAL_COSTS)) - currentCellCopy.getRoom().getCost());
                            if (currentRoom instanceof OfficeRoom) {
                                gameCache.setValue(TOTAL_INCOMES, Float.parseFloat((String) gameCache.getValue(TOTAL_INCOMES)) - 1000.0f);
                            }
                            if (currentStaffTypeCopy != null) {
                                setEmployeesAmountByType(currentStaffTypeCopy,
                                        getEmployeesAmountByType(currentStaffTypeCopy) - currentRoom.getStaff().size);
                            }
                            gameCache.setValue(TOTAL_SALARIES, Float.parseFloat((String) gameCache.getValue(TOTAL_SALARIES))
                                    - currentRoom.getStaff().size * currentStaffTypeSalaryCopy);
                            setRoomsAmountByType(currentCellCopy.getRoom().getType(),
                                    getRoomsAmountByType(currentCellCopy.getRoom().getType()) - 1L);
                            gameCache.setValue(CURRENT_ROOM, null);
                            currentCellCopy.setRoom(null);
                            roomInfo.setVisible(false);
                            return true;
                        }
                        return false;
                    }
                });
                roomInfo.add(roomInfoLabel).center().expand();
                roomInfo.row();
                roomInfo.add(destroy).center().expand();
                roomInfo.setSize(roomInfo.getPrefWidth(), roomInfo.getPrefHeight());
                roomInfo.setPosition(Gdx.graphics.getWidth() - roomInfo.getWidth() - 20, budget.getY() - 20 - roomInfo.getHeight());
                roomInfo.setVisible(true);
            }
        }
    }

    private void createToolPane() {
        this.officeStatLabel = new Label("Rooms:", skin);
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
        this.officeStatWindow.setResizable(true);
        this.officeStatWindow.add(this.officeStatLabel).center().expand();
        this.officeStatWindow.getTitleTable().add(hideOfficeStatWindow).right();
        this.officeStatWindow.setVisible(false);
        setRoomsStat();
        this.officeStatWindow.setSize(this.officeStatWindow.getPrefWidth(), this.officeStatWindow.getPrefHeight());
        this.officeStatWindow.setPosition(this.budget.getX(), this.budget.getY() - 20 - this.officeStatWindow.getHeight());
        this.roomInfo = new Window("Room Info", skin);
        TextButton hideRoomInfo = new TextButton("x", skin);
        hideRoomInfo.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                roomInfo.setVisible(false);
                return false;
            }
        });
        this.roomInfo.setMovable(true);
        this.roomInfo.setResizable(true);
        this.roomInfo.getTitleTable().add(hideRoomInfo).right();
        this.roomInfo.setVisible(false);
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
                return Long.parseLong((String) gameCache.getValue(OFFICES_AMOUNT));
            case HALL:
                return Long.parseLong((String) gameCache.getValue(HALLS_AMOUNT));
            case SECURITY:
                return Long.parseLong((String) gameCache.getValue(SECURITY_AMOUNT));
            case CLEANING:
                return Long.parseLong((String) gameCache.getValue(CLEANING_AMOUNT));
            default:
                return -1L;
        }
    }

    private void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE:
                gameCache.setValue(OFFICES_AMOUNT, amount);
                break;
            case HALL:
                gameCache.setValue(HALLS_AMOUNT, amount);
                break;
            case SECURITY:
                gameCache.setValue(SECURITY_AMOUNT, amount);
                break;
            case CLEANING:
                gameCache.setValue(CLEANING_AMOUNT, amount);
                break;
            default:
                break;
        }
    }

    private long getEmployeesAmountByType(Staff.Type type) {
        switch (type) {
            case SECURITY: return Long.parseLong((String) gameCache.getValue(TOTAL_SECURITY_STAFF));
            case WORKER: return Long.parseLong((String) gameCache.getValue(TOTAL_WORKERS));
            case CLEANING: return Long.parseLong((String) gameCache.getValue(TOTAL_CLEANING_STAFF));
            case ADMINISTRATION: return Long.parseLong((String) gameCache.getValue(TOTAL_ADMIN_STAFF));
            default: return -1L;
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
        this.notification.addAction(sequence(delay(4f), hide()));
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

}
