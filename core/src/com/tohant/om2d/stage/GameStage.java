package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.Map;
import com.tohant.om2d.actor.Room;
import com.tohant.om2d.storage.Cache;

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
    private Label roomsStat;

    public GameStage(float budget, String time, Viewport viewport, Batch batch) {
        super(viewport, batch);
        Grid grid = new Grid((int) ((Gdx.graphics.getWidth() / 2f) - (GRID_SIZE / 2)),
                ((int) ((Gdx.graphics.getHeight() / 2f) - (GRID_SIZE / 2))),
                GRID_SIZE, GRID_SIZE, (int) GRID_SIZE / 15);
        map = new Map(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), grid);
        addActor(map);
        Skin skin = getDefaultSkin();
        gameCache = Cache.getInstance();
        gameCache.setBudget(budget);
        this.budget = new Label(Math.round(budget) + " $", skin);
        this.budget.setPosition(20, Gdx.graphics.getHeight() - 60);
        this.budget.setSize(100, 50);
        this.budget.setColor(Color.GREEN);
        createToolPane(skin);
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
            int finalI = i;
            room.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchDown(event, x, y, pointer, button);
                    currentRoom = rooms[finalI];
                    gameCache.setRoomType(currentRoom);
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
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean childHandled = false;
        Actor actor = hit(screenX, screenY, true);
        if (actor != null) {
            try {
                childHandled = actor.fire(new InputEvent());
            } catch (RuntimeException ignored) {

            }
        }
        return childHandled || super.touchDown(screenX, screenY, pointer, button);
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
        this.roomsStat.setText(builder.toString());
    }

    private void createToolPane(Skin skin) {
        this.roomsStat = new Label("Rooms:", skin);
        setRoomsStat();
        TextButton hideModal = new TextButton("x", skin);
        hideModal.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                event.getTarget().getParent().getParent().getParent().setVisible(false);
                return false;
            }
        });
        this.officeStatWindow = new Window("Office", skin);
        this.officeStatWindow.setModal(true);
        this.officeStatWindow.setResizable(true);
        this.officeStatWindow.setModal(true);
        this.officeStatWindow.add(this.roomsStat).center();
        this.officeStatWindow.getTitleTable().add(hideModal).right();
        this.officeStatWindow.setVisible(false);
        this.officeStatWindow.setSize(this.officeStatWindow.getPrefWidth(), this.officeStatWindow.getPrefHeight());
        this.officeStatWindow.setPosition(this.budget.getX(), this.budget.getY() - 20 - this.officeStatWindow.getHeight());
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
        this.toolPane.add(officeButton);
        this.toolPane.getTitleTable().add(hide).right();
    }

    public Map getMap() {
        return map;
    }
}
