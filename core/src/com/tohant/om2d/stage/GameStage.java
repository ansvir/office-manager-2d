package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Map;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.UiActorService;

public class GameStage extends AbstractStage {

    private final Vector2 lastDragPos;
    private final AssetService assetService;
    private final UiActorService uiActorService;

    public GameStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        assetService = AssetService.getInstance();
        lastDragPos = new Vector2();
        uiActorService = UiActorService.getInstance();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        lastDragPos.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        super.touchDragged(screenX, screenY, pointer);
        Map map = (Map) uiActorService.getActorById(UiActorService.UiComponentConstant.MAP.name());
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
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.AllResize);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        Gdx.graphics.setCursor(assetService.getDefaultCursor());
        return false;
    }

    @Override
    public void dispose() {
    }

}
