package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.CacheSnapshotService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.stage.AbstractStage;
import com.tohant.om2d.stage.GameStage;
import com.tohant.om2d.stage.UiStage;
import com.tohant.om2d.storage.CacheProxy;
import com.tohant.om2d.storage.CachedEventListener;

import java.util.Map;

import static com.tohant.om2d.storage.CacheImpl.*;


public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private AbstractStage gameStage;
    private AbstractStage uiStage;
    private Viewport gameViewport;
    private Viewport uiViewport;
    private OrthographicCamera camera;
    private InputMultiplexer multiplexer;
    private TimeLineTask<Boolean> timeline;
    private String time;
    private AsyncExecutor executor;
    private CacheProxy gameCache;
    private CachedEventListener eventListener;
    private UiActorService uiActorService;
    private boolean isPayDay;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        gameViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(gameViewport.getScreenWidth(), gameViewport.getScreenHeight());
        gameViewport.setCamera(camera);
        gameCache = new CacheProxy();
        eventListener = CachedEventListener.getInstance();
        uiActorService = UiActorService.getInstance();
        gameStage = new GameStage(gameViewport, batch);
        uiStage = new UiStage(uiViewport, batch);
        for (Actor a : uiActorService.getUiActors()) {
            if (a instanceof WidgetGroup || a instanceof Widget) {
                uiStage.addActor(a);
            } else {
                gameStage.addActor(a);
            }
        }
        multiplexer = new InputMultiplexer(uiStage, gameStage);
        Gdx.input.setInputProcessor(multiplexer);
        executor = new AsyncExecutor(1);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        processTimeLine();
        updateBudget();
        uiStage.act();
        gameStage.act();
        gameStage.getViewport().setCamera(camera);
        gameStage.draw();
        uiStage.draw();
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
        gameStage.dispose();
        batch.dispose();
        timeline.forceFinish();
        eventListener.stop();
    }

    private void processTimeLine() {
        if (timeline != null && !timeline.isDone()) {
            time = timeline.getDateString();
            gameCache.setValue(CURRENT_TIME, time);
            ((UiStage) uiStage).setTime(time);
        } else {
            timeline = new TimeLineTask<>(500L, true);
            executor.submit(timeline);
        }
    }

    private void updateBudget() {
        if (this.timeline.getDate().getDays() == 1
                && !this.timeline.getDate().equals(new TimeLineDate(1L ,1L ,1L)) && !isPayDay) {
            Map<String, ?> cacheSnapshot = eventListener.consume();
            CacheSnapshotService snapshotService = new CacheSnapshotService(cacheSnapshot);
            float budget = snapshotService.getFloat(CURRENT_BUDGET);
            float salaries = calculateSalaries(snapshotService);
            float costs = calculateCosts(snapshotService);
            float incomes = calculateIncomes(snapshotService);
            gameCache.setValue(CURRENT_BUDGET, budget - costs - salaries + incomes);
            isPayDay = true;
        } else {
            isPayDay = false;
            eventListener.post();
        }
    }

    private float calculateCosts(CacheSnapshotService snapshotService) {
        float cleaningCost = snapshotService.getLong(CLEANING_AMOUNT) * Room.Type.CLEANING.getCost();
        float securityCost = snapshotService.getLong(SECURITY_AMOUNT) * Room.Type.SECURITY.getCost();
        float officeCost = snapshotService.getLong(OFFICES_AMOUNT) * Room.Type.OFFICE.getCost();
        float hallCost = snapshotService.getLong(HALLS_AMOUNT) * Room.Type.HALL.getCost();
        float caffeCost = snapshotService.getLong(CAFFE_AMOUNT) * Room.Type.CAFFE.getCost();
        float elevatorCost = snapshotService.getLong(ELEVATOR_AMOUNT) * Room.Type.ELEVATOR.getCost();
        return cleaningCost + securityCost + officeCost + hallCost + caffeCost + elevatorCost;
    }

    private float calculateSalaries(CacheSnapshotService snapshotService) {
        float securitySalaries = snapshotService.getLong(TOTAL_SECURITY_STAFF) * Staff.Type.SECURITY.getSalary();
        float cleaningSalaries = snapshotService.getLong(TOTAL_CLEANING_STAFF) * Staff.Type.CLEANING.getSalary();
        float caffeSalaries = snapshotService.getLong(TOTAL_CAFFE_STAFF) * Staff.Type.CAFFE.getSalary();
        return securitySalaries + cleaningSalaries + caffeSalaries;
    }

    private float calculateIncomes(CacheSnapshotService snapshotService) {
        return snapshotService.getLong(TOTAL_WORKERS) * 100.0f;
    }

}
