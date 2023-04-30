package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.Room;
import com.tohant.om2d.stage.GameMainStage;
import com.tohant.om2d.stage.GameUiStage;

public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private GameMainStage mainStage;
    private GameUiStage uiStage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private InputMultiplexer multiplexer;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(viewport.getScreenWidth(), viewport.getScreenHeight());
        mainStage = new GameMainStage(viewport, batch);
        uiStage = new GameUiStage(1200.0f);
        multiplexer = new InputMultiplexer(mainStage, uiStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        mainStage.draw();
        mainStage.act(delta);
        uiStage.draw();
        uiStage.act();
        batch.begin();
        batch.end();
        camera.update();
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

    }
}
