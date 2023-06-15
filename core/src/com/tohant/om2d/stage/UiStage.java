package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.command.office.UpdateBudgetCommand;
import com.tohant.om2d.command.office.UpdateOfficeInfoCommand;
import com.tohant.om2d.command.office.UpdateTimeCommand;
import com.tohant.om2d.command.room.UpdateRoomInfoCommand;
import com.tohant.om2d.command.ui.CreateNotificationCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.actor.constant.Constant.DAY_WAIT_TIME_MILLIS;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.NOTIFICATION_MODAL;
import static com.tohant.om2d.storage.Cache.CURRENT_BUDGET;

public class UiStage extends AbstractStage {

    private final RuntimeCacheService cacheService;
    private final UiActorService uiActorService;
    private float deltaTimestamp;

    public UiStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        cacheService = RuntimeCacheService.getInstance();
        uiActorService = UiActorService.getInstance();
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);
            checkForExceptionsAndThrowIfExist(0);
        } catch (Exception e) {
            if (e instanceof GameException) {
                Actor a = null;
                try {
                    a = getRoot().findActor(NOTIFICATION_MODAL.name());
                } catch (NullPointerException i) {
                }
                if (a != null) {
                    getRoot().removeActor(a);
                    new CreateNotificationCommand((GameException) e).execute();
                    addActor(uiActorService.getActorById(NOTIFICATION_MODAL.name()));
                }
            } else {
                throw e;
            }
        } finally {
            if (deltaTimestamp * 1000L >= DAY_WAIT_TIME_MILLIS) {
                deltaTimestamp = 0.0f;
            } else {
                deltaTimestamp += Gdx.graphics.getDeltaTime();
            }
            setBudget(cacheService.getFloat(CURRENT_BUDGET));
            new UpdateOfficeInfoCommand().execute();
            new UpdateRoomInfoCommand(deltaTimestamp).execute();
        }
    }

    public void setBudget(float budget) {
        new UpdateBudgetCommand(budget).execute();
    }

    public void setTime(String time) {
        new UpdateTimeCommand(time).execute();
    }

}
