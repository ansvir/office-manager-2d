package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.command.office.UpdateBudgetCommand;
import com.tohant.om2d.command.office.UpdateOfficeInfoCommand;
import com.tohant.om2d.command.office.UpdateTimeCommand;
import com.tohant.om2d.command.room.UpdateRoomInfoCommand;
import com.tohant.om2d.command.ui.RestoreDefaultsCommand;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.cache.Cache;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.tohant.om2d.actor.constant.Constant.DAY_WAIT_TIME_MILLIS;

public class UiStage extends AbstractStage {

    private final RuntimeCacheService cacheService;
    private float deltaTimestamp;

    public UiStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        cacheService = RuntimeCacheService.getInstance();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (deltaTimestamp * 1000L >= DAY_WAIT_TIME_MILLIS) {
            deltaTimestamp = 0.0f;
        } else {
            deltaTimestamp += Gdx.graphics.getDeltaTime();
        }
        setBudget(cacheService.getFloat(Cache.CURRENT_BUDGET));
        new UpdateOfficeInfoCommand().execute();
        new UpdateRoomInfoCommand(deltaTimestamp).execute();
    }

    @Override
    public boolean keyDown(int keyCode) {
        super.keyDown(keyCode);
        if (keyCode == ESCAPE) {
            new RestoreDefaultsCommand().execute();
        }
        return false;
    }

    public void setBudget(float budget) {
        new UpdateBudgetCommand(budget).execute();
    }

    public void setTime(String time) {
        new UpdateTimeCommand(time).execute();
    }

}
