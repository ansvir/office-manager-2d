package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.model.RoadType;
import com.tohant.om2d.service.AssetService;

import static com.tohant.om2d.actor.constant.Constant.*;

public class Background extends Group {

    private final AssetService assetService;
    private final Array<Road> roads;

    public Background(int x, int y, int width, int height) {
        setSize(width + Gdx.graphics.getWidth() - (CELL_SIZE * GRID_WIDTH), height + Gdx.graphics.getHeight() - (CELL_SIZE * GRID_HEIGHT));
        setPosition(x, y);
        this.assetService = AssetService.getInstance();
        roads = new Array<>();
        createRoads();
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
        for (int i = 0; i < roads.size; i++) {
            roads.get(i).draw(batch, parentAlpha);
        }
    }

    private Texture createBackground() {
        int width = Math.round(getWidth() / CELL_SIZE);
        int height = Math.round(getHeight() / CELL_SIZE);
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
                grassBackground.drawPixmap(grassPixmap, i * CELL_SIZE, j * CELL_SIZE);
            }
        }
        if (grassPixmap != null) {
            grassPixmap.dispose();
        }
        Texture bg = new Texture(grassBackground);
        grassBackground.dispose();
        return bg;
    }

    private void createRoads() {
        int width = Math.round(getWidth() / CELL_SIZE);
        int height = Math.round(getHeight() / CELL_SIZE);
        roads.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 5) {
                    Road road = new Road(i * CELL_SIZE, j * CELL_SIZE, RoadType.EMPTY);
                    roads.add(road);
                    addActor(road);
                }
                if (i == 6) {
                    Road road = new Road(i * CELL_SIZE, j * CELL_SIZE, RoadType.RIGHT);
                    roads.add(road);
                    addActor(road);
                }
                if (i == 7) {
                    Road road = new Road(i * CELL_SIZE, j * CELL_SIZE, RoadType.LEFT);
                    roads.add(road);
                    addActor(road);
                }
                if (i == 8) {
                    Road road = new Road(i * CELL_SIZE, j * CELL_SIZE, RoadType.EMPTY);
                    roads.add(road);
                    addActor(road);
                }
            }
        }
    }
}
