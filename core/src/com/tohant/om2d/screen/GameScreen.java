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
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.CacheService;
import com.tohant.om2d.service.CacheSnapshotService;
import com.tohant.om2d.stage.GameStage;
import com.tohant.om2d.storage.CacheProxy;
import com.tohant.om2d.storage.CachedEventListener;

import java.util.Map;

import static com.tohant.om2d.storage.CacheImpl.*;


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
    private CacheService cacheService;
    private boolean isPayDay;
    private boolean isPaid;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(viewport.getScreenWidth(), viewport.getScreenHeight());
        viewport.setCamera(camera);
        asyncExecutor = new AsyncExecutor(1);
        timeString = asyncExecutor.submit(timeline);
        time = "01/01/0001";
        gameCache = new CacheProxy();
        cacheService = new CacheService(gameCache);
        eventListener = CachedEventListener.getInstance();
        gameStage = new GameStage(time, viewport, batch);
        multiplexer = new InputMultiplexer(gameStage);
        Gdx.input.setInputProcessor(multiplexer);
        isPaid = true;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        processTimeLine();
        updateBudget();
        gameStage.draw();
        gameStage.act(delta);
        batch.begin();
        batch.setColor(Color.WHITE);
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
            time = timeline.getDate();
            gameCache.setValue(CURRENT_TIME, time);
            gameStage.setTime(time);
        } else {
            timeline = new TimeLineTask(500L);
            timeString = asyncExecutor.submit(timeline);
        }
    }

    private void updateBudget() {
        if (this.timeline.getCurrentDay() == 1L && !isPayDay && !isPaid) {
            Map<String, ?> cacheSnapshot = eventListener.consume();
            CacheSnapshotService snapshotService = new CacheSnapshotService(cacheSnapshot);
            float budget = snapshotService.getFloat(CURRENT_BUDGET);
            float salaries = calculateSalaries(snapshotService);
            float costs = calculateCosts(snapshotService);
            float incomes = calculateIncomes(snapshotService);
            gameCache.setValue(CURRENT_BUDGET, budget - costs - salaries + incomes);
            isPayDay = true;
            isPaid = true;
        } else {
            isPayDay = false;
            isPaid = false;
            eventListener.post();
        }
    }

    private float calculateCosts(CacheSnapshotService snapshotService) {
        float cleaningCost = snapshotService.getLong(CLEANING_AMOUNT) * Room.Type.CLEANING.getCost();
        float securityCost = snapshotService.getLong(SECURITY_AMOUNT) * Room.Type.SECURITY.getCost();
        float officeCost = snapshotService.getLong(OFFICES_AMOUNT) * Room.Type.OFFICE.getCost();
        float hallCost = snapshotService.getLong(HALLS_AMOUNT) * Room.Type.HALL.getCost();
        return cleaningCost + securityCost + officeCost + hallCost;
    }

    private float calculateSalaries(CacheSnapshotService snapshotService) {
        float securitySalaries = snapshotService.getLong(TOTAL_SECURITY_STAFF) * Staff.Type.SECURITY.getSalary();
        float cleaningSalaries = snapshotService.getLong(TOTAL_CLEANING_STAFF) * Staff.Type.CLEANING.getSalary();
        return securitySalaries + cleaningSalaries;
    }

    private float calculateIncomes(CacheSnapshotService snapshotService) {
        return snapshotService.getLong(TOTAL_WORKERS) * 100.0f;
    }

}
