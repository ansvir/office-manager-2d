package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Room;

import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class GameUiStage extends Stage implements InputProcessor {

    private Label budget;
    private Array<TextButton> roomsButtons;
    private Window help;
    private Label helpLabel;
    private Room.Type currentRoom;

    public GameUiStage(float budget) {
        Skin skin = getDefaultSkin();
        this.budget = new Label(Math.round(budget) + " $", skin);
        this.budget.setPosition(20, Gdx.graphics.getHeight() - 60);
        this.budget.setSize(100, 50);
        this.budget.setColor(Color.GREEN);
        roomsButtons = new Array<>();
        Room.Type[] rooms = Room.Type.values();
        float buttonWidth = 200f;
        float buttonHeight = 60f;
        for (int i = 0, j = 40; i < rooms.length; i++, j += buttonHeight + 20) {
            TextButton room = new TextButton(rooms[i].name(), skin);
            room.setSize(buttonWidth, buttonHeight);
            room.setX(Gdx.graphics.getWidth() - (buttonWidth + 20));
            room.setY(j);
            int finalI = i;
            room.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentRoom = rooms[finalI];
                }
            });
            addActor(room);
            roomsButtons.add(room);
        }

        addActor(this.budget);
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

}
