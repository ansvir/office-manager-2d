package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class MenuScreen implements Screen {

    private Game game;
    private TextButton start;
    private Skin skin;
    private Texture background;
    private Color bgColor;
    private SpriteBatch batch;
    private Stage stage;
    private Label title;
    private Label title2;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        skin = getDefaultSkin();
        batch = new SpriteBatch();
        stage = new Stage();
        start = new TextButton("START", skin);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        start.setPosition(20, 40);
        bgColor = Color.valueOf("a0dcef");
        background = new Texture("bg.png");
        title = new Label("Office Manager", skin);
        title.setFontScale(2f, 2f);
        title.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 3.5f,
                Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 7f);
        title2 = new Label("2D", skin);
        title2.setFontScale(3f, 3f);
        title2.setColor(Color.RED);
        title2.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 3.5f,
                Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4f);
        stage.addActor(start);
        stage.addActor(title);
        stage.addActor(title2);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(bgColor);
        batch.begin();
        batch.draw(background, Gdx.graphics.getWidth() / 2f - background.getWidth() / 2f, background.getHeight() / -10f);
        batch.end();
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        batch.dispose();
        background.dispose();
        stage.dispose();
    }
}
