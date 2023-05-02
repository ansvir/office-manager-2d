package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.stage.GameStage;
import com.tohant.om2d.storage.GameCache;


public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private GameStage gameStage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private InputMultiplexer multiplexer;
    private TimeLineTask timeline;
    private AsyncExecutor asyncExecutor;
    private AsyncResult<String> timeString;
    private String time;
    private GameCache gameCache;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(viewport.getScreenWidth(), viewport.getScreenHeight());
        viewport.setCamera(camera);
        gameCache = new GameCache();
        asyncExecutor = new AsyncExecutor(1);
        timeString = asyncExecutor.submit(timeline);
        time = "01/01/0001";
        gameCache.setTime(time);
        gameStage = new GameStage(2000.0f, time, viewport, batch);
        multiplexer = new InputMultiplexer(gameStage);
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        processTimeLine();
        gameStage.draw();
        gameStage.act(delta);
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
        asyncExecutor.dispose();
        gameStage.dispose();
        batch.dispose();
        timeline.forceFinish();
    }

    private void processTimeLine() {
        if (!timeString.isDone()) {
            time = timeline.get();
            gameCache.setTime(time);
            gameStage.setTime(time);
        } else {
            timeline = new TimeLineTask(500L);
            timeString = asyncExecutor.submit(timeline);
        }
    }

    private void updateCosts() {
        if (Long.parseLong(time.substring(time.lastIndexOf("DAY ") + 1)) % 30 == 0) {
            gameCache.setBudget(gameCache.getBudget());
        }
    }

}
