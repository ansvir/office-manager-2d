package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Room;
import com.tohant.om2d.storage.GameCache;

import static com.badlogic.gdx.utils.Align.left;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class GameUiStage extends Stage implements InputProcessor {

    private Label budget;
    private Array<TextButton> roomsButtons;
    private Room.Type currentRoom;
    private GameCache gameCache;
    private Label time;
    private Window toolPane;
    private Window officeStatWindow;
    private Label roomsStat;

    public GameUiStage(float budget, String time) {
        Skin skin = getDefaultSkin();
        gameCache = new GameCache();
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
            room.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    currentRoom = rooms[finalI];
                    gameCache.setRoomType(currentRoom);
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
        hideModal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                event.getTarget().getParent().setVisible(false);
            }
        });
        this.officeStatWindow = new Window("Office", skin);
        this.officeStatWindow.setModal(true);
        this.officeStatWindow.setResizable(true);
        this.officeStatWindow.setModal(true);
        this.officeStatWindow.setPosition(this.budget.getX(), this.budget.getY() - 20 - this.officeStatWindow.getHeight());
        this.officeStatWindow.add(this.roomsStat).center();
        this.officeStatWindow.getTitleTable().add(hideModal).right();
        this.officeStatWindow.setVisible(false);
        this.toolPane = new Window("Office Manager 2D", skin);
        this.toolPane.setSize(Gdx.graphics.getWidth(), 150f);
        this.toolPane.setPosition(0, 0);
        this.toolPane.setMovable(false);
        this.toolPane.setResizable(false);
        this.toolPane.align(left);
        TextButton hide = new TextButton("-", skin);
        hide.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                toolPane.setHeight(toolPane.getTouchable() == Touchable.enabled ? 50f : 150f);
                toolPane.setTouchable(toolPane.getTouchable() == Touchable.enabled
                        ? Touchable.childrenOnly : Touchable.enabled);
            }
        });
        TextButton officeButton = new TextButton("Office", skin);
        officeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                officeStatWindow.setVisible(!officeStatWindow.isVisible());
            }
        });
        addActor(this.officeStatWindow);
        this.toolPane.add(officeButton);
        this.toolPane.getTitleTable().add(hide).right();
    }

}
