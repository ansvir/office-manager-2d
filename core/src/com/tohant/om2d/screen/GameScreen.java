package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.stage.GameStage;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.storage.CachedEventListener;

import java.util.Map;

import static com.tohant.om2d.storage.Cache.IS_PAYDAY;
import static com.tohant.om2d.storage.Cache.TOTAL_COSTS;


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
    private Cache gameCache;
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
        gameCache = Cache.getInstance();
        initGameCache();
        eventListener = CachedEventListener.getInstance();
        asyncExecutor = new AsyncExecutor(1);
        timeString = asyncExecutor.submit(timeline);
        time = "01/01/0001";
        gameCache.setTime(time);
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
            gameCache.setTime(time);
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
            gameCache.setBoolean(IS_PAYDAY, false);
            gameCache.setBudget(gameCache.getBudget() - totalCosts);
            gameCache.setFloat(TOTAL_COSTS, 0.0f);
            eventListener.post();
        }
    }

    private void initGameCache() {
        gameCache.setFloat(TOTAL_COSTS, 0.0f);
    }

}
