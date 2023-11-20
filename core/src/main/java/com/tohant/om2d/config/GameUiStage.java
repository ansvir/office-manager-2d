package com.tohant.om2d.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tohant.om2d.command.office.UpdateBudgetCommand;
import com.tohant.om2d.command.office.UpdateOfficeInfoCommand;
import com.tohant.om2d.command.room.UpdateRoomInfoCommand;
import com.tohant.om2d.command.ui.CreateNotificationCommand;
import com.tohant.om2d.command.ui.RestoreDefaultsCommand;
import com.tohant.om2d.di.annotation.BeanType;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.tohant.om2d.actor.constant.Constant.DAY_WAIT_TIME_MILLIS;

@Component(BeanType.PROTOTYPE)
@RequiredArgsConstructor
public class GameUiStage extends Stage {

    private final GameViewport gameViewport;
    private final GameCache gameCache;
    private final UpdateBudgetCommand updateBudgetCommand;
    private final UpdateRoomInfoCommand updateRoomInfoCommand;
    private final UpdateOfficeInfoCommand updateOfficeInfoCommand;
    private final RestoreDefaultsCommand restoreDefaultsCommand;
    private final GameActorFactory gameActorFactory;
    private final CreateNotificationCommand createNotificationCommand;

    private float deltaTimestamp;

    @PostConstruct
    public void init() {
        setViewport(gameViewport);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (deltaTimestamp * 1000L >= DAY_WAIT_TIME_MILLIS) {
            deltaTimestamp = 0.0f;
        } else {
            deltaTimestamp += Gdx.graphics.getDeltaTime();
        }
        gameCache.setFloat(GameCache.CURRENT_DELTA_TIME, deltaTimestamp);
        updateBudgetCommand.execute();
        updateOfficeInfoCommand.execute();
        updateRoomInfoCommand.execute();
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);
            checkForExceptionsAndThrowIfExist(0);
        } catch (Exception e) {
            if (e instanceof GameException) {
                getRoot().findActor(GameActorFactory.UiComponentConstant.NOTIFICATION_MODAL.name()).remove();
                addActor(gameActorFactory.createNotificationModal());
                createNotificationCommand.execute();
            } else {
                throw e;
            }
        }
    }

    @Override
    public boolean keyDown(int keyCode) {
        super.keyDown(keyCode);
        if (keyCode == ESCAPE) {
            restoreDefaultsCommand.execute();
        }
        return false;
    }

    private void checkForExceptionsAndThrowIfExist(int i) {
        if (exceptions.size > 0 && i < exceptions.size) {
            checkForExceptionsAndThrowIfExist(i + 1);
            GameException e = exceptions.get(i);
            exceptions.removeIndex(i);
            throw e;
        }
    }

}
