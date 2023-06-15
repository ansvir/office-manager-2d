package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.service.AssetService;

public class GameStage extends AbstractStage {

    private final Vector2 lastDragPos;
    private final AssetService assetService;

    public GameStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        assetService = AssetService.getInstance();
        lastDragPos = new Vector2();
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
        float deltaX = lastDragPos.x - screenX;
        float deltaY = screenY - lastDragPos.y;
        OrthographicCamera camera = (OrthographicCamera) getCamera();
        camera.position.add(deltaX * camera.zoom, deltaY * camera.zoom, 0);
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
