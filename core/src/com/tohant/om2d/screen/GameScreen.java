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
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.storage.CacheImpl;
import com.tohant.om2d.storage.CacheProxy;
import com.tohant.om2d.storage.CachedEventListener;

import java.util.Map;

import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.storage.CacheImpl.CURRENT_ROOM;


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
    private CacheProxy gameCache;
    private CachedEventListener eventListener;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(viewport.getScreenWidth(), viewport.getScreenHeight());
        viewport.setCamera(camera);
        eventListener = CachedEventListener.getInstance();
        asyncExecutor = new AsyncExecutor(1);
        timeString = asyncExecutor.submit(timeline);
        time = "01/01/0001";
        gameCache = new CacheProxy((c) -> {}, (c) -> {}, (c) -> {
            c.setValue(CURRENT_ROOM_TYPE, null);
            c.setValue(CURRENT_BUDGET, 2000.0f);
            c.setValue(CURRENT_TIME, "01/01/0001");
            c.setValue(OFFICES_AMOUNT, 0L);
            c.setValue(HALLS_AMOUNT, 0L);
            c.setValue(SECURITY_AMOUNT, 0L);
            c.setValue(CLEANING_AMOUNT, 0L);
            c.setValue(IS_PAYDAY, false);
            c.setValue(CURRENT_ROOM, null);
            c.setValue(TOTAL_COSTS, 0.0f);
        });
        gameStage = new GameStage(2000.0f, time, viewport, batch);
        multiplexer = new InputMultiplexer(gameStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        processTimeLine();
        updateCosts();
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
        eventListener.stop();
    }

    private void processTimeLine() {
        if (!timeString.isDone()) {
            time = timeline.get();
            gameCache.setValue(CURRENT_TIME, time);
            gameStage.setTime(time);
        } else {
            timeline = new TimeLineTask(500L);
            timeString = asyncExecutor.submit(timeline);
        }
    }

    private void updateCosts() {
        Map<String, ?> cacheSnapshot = eventListener.consume();
        boolean isPayday = false;
        if (cacheSnapshot != null) {
            isPayday = Boolean.parseBoolean((String) cacheSnapshot.get(IS_PAYDAY));
        }
        if (isPayday) {
            float totalCosts = Float.parseFloat((String) cacheSnapshot.get(TOTAL_COSTS));
//            for (Actor a : gameStage.getMap().getGrid().getChildren().items) {
//                if (a instanceof Cell) {
//                    if (!((Cell) a).isEmpty()) {
//                        totalCosts += ((Cell) a).getRoom().getCost();
//                    }
//                }
//            }
            gameCache.setValue(IS_PAYDAY, false);
            gameCache.setValue(CURRENT_BUDGET, Float.parseFloat(((String) gameCache.getValue(CURRENT_BUDGET))) - totalCosts);
            gameCache.setValue(TOTAL_COSTS, 0.0f);
            eventListener.post();
        }
    }

}
