package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.office.UpdatePeopleCommand;
import com.tohant.om2d.command.office.UpdateTimeCommand;
import com.tohant.om2d.config.GameViewport;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.model.entity.CompanyEntity;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.model.office.OfficeInfo;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.CacheSnapshotService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.config.GameStage;
import com.tohant.om2d.config.GameUiStage;
import com.tohant.om2d.storage.cache.CachedEventListener;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.CompanyDao;
import com.tohant.om2d.storage.database.ProgressDao;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class GameScreen implements Screen {

    private final GameCache gameCache;
    private final GameActorFactory gameActorFactory;
    private final GameScreen gameScreen;
    private final UpdateTimeCommand updateTimeCommand;
    private final UpdatePeopleCommand updatePeopleCommand;
    private final GameViewport gameViewport;
    private final GameUiStage gameUiStage;
    private final GameStage gameStage;
    private final CachedEventListener cachedEventListener;
    private final ProgressDao progressDao;
    private final CompanyDao companyDao;

    private Game game;
    private OrthographicCamera camera;
    private InputMultiplexer multiplexer;
    private TimeLineTask<Boolean> timeline;
    private String time;
    private AsyncExecutor executor;
    private boolean isPayDay;
//    private Staff[] men;

    @Override
    public void show() {
        camera = new OrthographicCamera(gameViewport.getScreenWidth(), gameViewport.getScreenHeight());
        gameViewport.setCamera(camera);
        gameActorFactory.initGameScreen();
        for (Actor a : gameActorFactory.getGameActors()) {
            if (a instanceof WidgetGroup || a instanceof Widget) {
                gameUiStage.addActor(a);
            } else {
                gameStage.addActor(a);
            }
        }
        multiplexer = new InputMultiplexer(gameUiStage, gameStage);
        Gdx.input.setInputProcessor(multiplexer);
        executor = new AsyncExecutor(1);
        AssetService.BG_MUSIC.play();
        gameCache.setObject(GameCache.OFFICE_INFO, new OfficeInfo("Office Inc."));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        update();
        gameUiStage.act();
        gameStage.act();
        gameStage.getViewport().setCamera(camera);
        gameStage.draw();
        gameUiStage.draw();
        gameStage.getBatch().begin();
        gameStage.getBatch().setColor(Color.WHITE);
        gameStage.getBatch().end();
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
        gameStage.getBatch().dispose();
        timeline.forceFinish();
        cachedEventListener.stop();
        AssetService.BG_MUSIC.stop();
    }

    private void update() {
        processTimeLine();
        updateOfficeInfo();
        updateEntities();
        checkIsReadyToSwitch();
    }

    private void processTimeLine() {
        if (timeline != null && !timeline.isFinished()) {
            time = timeline.getDateString();
            gameCache.setValue(GameCache.CURRENT_TIME, time);
            updateTimeCommand.execute();
        } else {
            ProgressEntity progressEntity = progressDao.queryForId(UUID.fromString(gameCache.getValue(GameCache.CURRENT_PROGRESS_ID)));
            TimeLineDate timeLineDate = new TimeLineDate(progressEntity.getTimeline());
            if (timeLineDate.equals(new TimeLineDate("01/01/0001"))) {
                timeline = new TimeLineTask<>(500L, updatePeopleCommand::execute, true);
                executor.submit(timeline);
            } else {
                timeline = new TimeLineTask<>(timeLineDate, 500L, updatePeopleCommand::execute, true);
                executor.submit(timeline);
            }
        }
    }

    private void updateOfficeInfo() {
        cachedEventListener.onEventConditional(snapshotService -> {
            float budget = snapshotService.getFloat(GameCache.CURRENT_BUDGET);
            float salaries = calculateSalaries(snapshotService);
            float costs = calculateCosts(snapshotService);
            float incomes = calculateIncomes(snapshotService);
            gameCache.setFloat(GameCache.CURRENT_BUDGET, budget - costs - salaries + incomes);
            isPayDay = true;
        }, () -> isPayDay = false, this.timeline.getDate().getDays() == 1
                && !this.timeline.getDate().equals(new TimeLineDate(1L, 1L, 1L)) && !isPayDay);
    }

    private float calculateCosts(CacheSnapshotService snapshotService) {
        float cleaningCost = snapshotService.getLong(GameCache.CLEANING_AMOUNT) * Room.Type.CLEANING.getCost();
        float securityCost = snapshotService.getLong(GameCache.SECURITY_AMOUNT) * Room.Type.SECURITY.getCost();
        float officeCost = snapshotService.getLong(GameCache.OFFICES_AMOUNT) * Room.Type.OFFICE.getCost();
        float hallCost = snapshotService.getLong(GameCache.HALLS_AMOUNT) * Room.Type.HALL.getCost();
        float caffeCost = snapshotService.getLong(GameCache.CAFFE_AMOUNT) * Room.Type.CAFFE.getCost();
        float elevatorCost = snapshotService.getLong(GameCache.ELEVATOR_AMOUNT) * Room.Type.ELEVATOR.getCost();
        return cleaningCost + securityCost + officeCost + hallCost + caffeCost + elevatorCost;
    }

    private float calculateSalaries(CacheSnapshotService snapshotService) {
        float securitySalaries = snapshotService.getLong(GameCache.TOTAL_SECURITY_STAFF) * Staff.Type.SECURITY.getSalary();
        float cleaningSalaries = snapshotService.getLong(GameCache.TOTAL_CLEANING_STAFF) * Staff.Type.CLEANING.getSalary();
        float caffeSalaries = snapshotService.getLong(GameCache.TOTAL_CAFFE_STAFF) * Staff.Type.CAFFE.getSalary();
        return securitySalaries + cleaningSalaries + caffeSalaries;
    }

    private float calculateIncomes(CacheSnapshotService snapshotService) {
        return snapshotService.getLong(GameCache.TOTAL_WORKERS) * 100.0f;
    }

    private void updateEntities() {
        ProgressEntity progressEntity = progressDao
                .queryForId(UUID.fromString(gameCache.getValue(GameCache.CURRENT_PROGRESS_ID)));
        float budget = gameCache.getFloat(GameCache.CURRENT_BUDGET);
        String currentTimeline = gameCache.getValue(GameCache.CURRENT_TIME);
        CompanyEntity companyEntity = companyDao.queryForId(progressEntity.getCompanyEntity().getId());
        companyEntity.setBudget(budget);
        companyDao.update(companyEntity);
        progressEntity.setTimeline(currentTimeline);
        progressDao.update(progressEntity);
    }

    private void checkIsReadyToSwitch() {
        if (gameCache.getBoolean(GameCache.READY_TO_START)) {
            gameCache.setBoolean(GameCache.READY_TO_START, false);
            AssetService.BG_MUSIC.stop();
            game.setScreen(gameScreen);
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
