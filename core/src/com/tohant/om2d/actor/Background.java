package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.service.AssetService;

import static com.tohant.om2d.actor.constant.Constant.*;

public class Background extends Actor {

    private final AssetService assetService;

    public Background(int x, int y, int width, int height) {
        setSize(width + Gdx.graphics.getWidth() - (CELL_SIZE * GRID_WIDTH), height + Gdx.graphics.getHeight() - (CELL_SIZE * GRID_HEIGHT));
        setPosition(x, y);
        this.assetService = AssetService.getInstance();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Texture bg = assetService.getBackground();
        if (bg == null) {
            bg = createBackground();
            assetService.setBackground(bg);
        }
        batch.draw(bg, getX(), getY());
    }

    private Texture createBackground() {
        int width = Math.round(getWidth() / 64f);
        int height = Math.round(getHeight() / 64f);
        Texture grass1 = assetService.getGrass1Texture();
        Texture grass2 = assetService.getGrass2Texture();
        Pixmap grassBackground = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Pixmap grassPixmap = null;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int next = MathUtils.random(1);
                if (next == 0) {
                    if (!grass1.getTextureData().isPrepared()) {
                        grass1.getTextureData().prepare();
                    }
                    grassPixmap = grass1.getTextureData().consumePixmap();
                } else {
                    if (!grass2.getTextureData().isPrepared()) {
                        grass2.getTextureData().prepare();
                    }
                    grassPixmap = grass2.getTextureData().consumePixmap();
                }
                grassBackground.drawPixmap(grassPixmap, i * 64, j * 64);
            }
        }
        if (grassPixmap != null) {
            grassPixmap.dispose();
        }
        Texture bg = new Texture(grassBackground);
        grassBackground.dispose();
        return bg;
    }

}
