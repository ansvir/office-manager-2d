package com.tohant.om2d.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Map;
import com.tohant.om2d.command.ui.CreateNotificationCommand;
import com.tohant.om2d.config.GameViewport;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameStage extends Stage {

    private final GameCache gameCache;
    private final GameViewport gameViewport;
    private final CreateNotificationCommand createNotificationCommand;
    private final GameActorFactory gameActorFactory;

    private Vector2 lastDragPos;
    private Array<GameException> exceptions;

    @PostConstruct
    public void init() {
        lastDragPos = new Vector2();
        setViewport(gameViewport);
        exceptions = (Array<GameException>) gameCache.getObject(GameCache.GAME_EXCEPTION);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        super.touchDragged(screenX, screenY, pointer);
        Map map = (Map) gameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.MAP.name());
        float deltaX = lastDragPos.x - screenX;
        float deltaY = screenY - lastDragPos.y;
        OrthographicCamera camera = (OrthographicCamera) getCamera();
        Vector3 deltaVector = new Vector3(
                Math.round(MathUtils.clamp(camera.position.x + (deltaX * camera.zoom),
                        map.getX() + camera.viewportWidth / 2f, map.getX() + map.getWidth() - camera.viewportWidth / 2f)),
                Math.round(MathUtils.clamp(camera.position.y + (deltaY * camera.zoom),
                        map.getY() + camera.viewportHeight / 2f, map.getY() + map.getHeight() - camera.viewportHeight / 2f)),
                0);
        camera.position.set(deltaVector);
        camera.update();
        lastDragPos.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        super.scrolled(amountX, amountY);
        float newZoom = MathUtils.lerp(
                ((OrthographicCamera) getCamera()).zoom,
                ((OrthographicCamera) getCamera()).zoom + (amountY * Gdx.graphics.getDeltaTime()),
                3.0f);
        newZoom = MathUtils.clamp(newZoom, 0.1f, 1.0f);
        ((OrthographicCamera) getCamera()).zoom = newZoom;
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastDragPos.set(screenX, screenY);
        boolean childHandled = false;
        Actor actor = hit(screenX, screenY, false);
        if (actor != null) {
            try {
                childHandled = actor.fire(new InputEvent());
            } catch (RuntimeException e) {
                if (e instanceof GameException) {
                    exceptions.add((GameException) e);
                } else {
                    throw e;
                }
            }
        }
        boolean superTouchDown = false;
        try {
            superTouchDown = super.touchDown(screenX, screenY, pointer, button);
        } catch (RuntimeException e) {
            if (e instanceof GameException) {
                exceptions.add((GameException) e);
            } else {
                throw e;
            }
        }
        return childHandled || superTouchDown;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        if (gameCache.getObject(GameCache.CURRENT_ITEM) == null) {
            AssetService.setCursor(AssetService.GameCursor.MAIN);
        }
        return false;
    }

    @Override
    public void dispose() {
    }

}
