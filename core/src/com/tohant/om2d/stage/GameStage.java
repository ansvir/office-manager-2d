package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.Map;
import com.tohant.om2d.actor.Room;
import com.tohant.om2d.storage.Cache;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class GameStage extends Stage {

    private static final float GRID_SIZE = 1000;

    private Map map;
    private Label budget;
    private Array<TextButton> roomsButtons;
    private Room.Type currentRoom;
    private Cache gameCache;
    private Label time;
    private Window toolPane;
    private Window officeStatWindow;
    private Label roomsStatLabel;
    private Window roomInfo;
    private Skin skin;

    public GameStage(float budget, String time, Viewport viewport, Batch batch) {
        super(viewport, batch);
        Grid grid = new Grid((int) ((Gdx.graphics.getWidth() / 2f) - (GRID_SIZE / 2)),
                ((int) ((Gdx.graphics.getHeight() / 2f) - (GRID_SIZE / 2))),
                GRID_SIZE, GRID_SIZE, (int) GRID_SIZE / 15);
        map = new Map(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), grid);
        addActor(map);
        skin = getDefaultSkin();
        gameCache = Cache.getInstance();
        gameCache.setBudget(budget);
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
                    gameCache.setCurrentRoomType(currentRoom);
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
        super.act(delta);
        setBudget(gameCache.getBudget());
        setRoomsStat();
//        setZIndices();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean childHandled = false;
        Actor actor = hit(screenX, screenY, false);
        if (actor != null) {
            try {
                if (actor instanceof Cell) {
                    updateRoomInfoWindow();
                }
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
            builder.append(gameCache.getRoomsAmountByType(t));
            builder.append("\n");
        }
        this.roomsStatLabel.setText(builder.toString());
    }

    private void updateRoomInfoWindow() {
        Cell currentCell = null;
        String id = gameCache.getValue(Cache.CURRENT_ROOM);
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
                String name = currentRoom.getType().name().charAt(0) +
                        currentRoom.getType().name().substring(1).toLowerCase();
                roomInfo.getTitleLabel().setText(name + " #" + currentRoom.getNumber());
                roomInfo.clearChildren();
                Label roomInfoLabel = new Label("Price: " + Math.round(currentRoom.getPrice()) + "$\n"
                        + "Cost: " + Math.round(currentRoom.getCost()) + "$/m",
                        skin);
                TextButton destroy = new TextButton("Destroy", skin);
                Cell currentCellCopy = currentCell;
                destroy.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        gameCache.setFloat(Cache.TOTAL_COSTS, gameCache.getFloatValue(Cache.TOTAL_COSTS) - currentCellCopy.getRoom().getCost());
                        currentCellCopy.setEmpty(true);
                        currentCellCopy.setRoom(null);
                        roomInfo.setVisible(false);
                        return true;
                    }
                });
                roomInfo.add(roomInfoLabel).align(center);
                roomInfo.row();
                roomInfo.add(destroy);
                roomInfo.setSize(roomInfo.getPrefWidth(), roomInfo.getPrefHeight());
                roomInfo.setPosition(Gdx.graphics.getWidth() - roomInfo.getWidth() - 20, budget.getY() - 20 - roomInfo.getHeight());
                roomInfo.setVisible(true);
            }
        }
    }

    private void createToolPane() {
        this.roomsStatLabel = new Label("Rooms:", skin);
        setRoomsStat();
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
        this.officeStatWindow.add(this.roomsStatLabel).center();
        this.officeStatWindow.getTitleTable().add(hideOfficeStatWindow).right();
        this.officeStatWindow.setVisible(false);
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

    private void setZIndices() {
        this.map.setZIndex(1);
        this.budget.setZIndex(2);
        this.roomsButtons.forEach(b -> b.setZIndex(3));
        this.time.setZIndex(2);
        this.toolPane.setZIndex(2);
        this.officeStatWindow.setZIndex(map.getZIndex() + 1);
        this.roomInfo.setZIndex(map.getZIndex() + 1);
    }

}
