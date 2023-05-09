package com.tohant.om2d.service;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.util.AssetsUtil;

import static com.tohant.om2d.actor.constant.Constant.CELL_SIZE;

public class AssetService implements Disposable {

    private static AssetService instance;

    private final Texture ACTIVE_EMPTY_CELL;
    private final Texture HALL_ROOM;
    private final Texture SECURITY_ROOM;
    private final Texture OFFICE_ROOM;
    private final Texture CLEANING_ROOM;
    private final Texture ROOM_CONSTRUCTION;
    private final Texture GRASS1;
    private final Texture GRASS2;
    private Texture background;

    private AssetService() {
        this.ACTIVE_EMPTY_CELL = createActiveEmptyCellTexture();
        this.HALL_ROOM = createHallRoomTexture();
        this.SECURITY_ROOM = createSecurityRoomTexture();
        this.OFFICE_ROOM = createOfficeRoomTexture();
        this.CLEANING_ROOM = createCleaningRoomTexture();
        this.ROOM_CONSTRUCTION = createRoomConstructionTexture();
        this.GRASS1 = createGrass1Texture();
        this.GRASS2 = createGrass2Texture();
    }

    public static AssetService getInstance() {
        if (instance == null) {
            instance = new AssetService();
        }
        return instance;
    }

    private Texture createActiveEmptyCellTexture() {
        Pixmap cellPixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        Color bordersColor = Color.BLACK;
        bordersColor.a = 0.2f;
        cellPixmap.setColor(bordersColor);
        cellPixmap.fill();
        Texture result = new Texture(cellPixmap);
        cellPixmap.dispose();
        return result;
    }

    private Texture createHallRoomTexture() {
        return AssetsUtil.resizeTexture("hall.png", CELL_SIZE, CELL_SIZE);
    }

    private Texture createSecurityRoomTexture() {
        return AssetsUtil.resizeTexture("security.png", CELL_SIZE, CELL_SIZE);
    }

    private Texture createOfficeRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.OFFICE.getColor());
        pixmap.fillRectangle(1, 1, pixmap.getWidth() - 1, pixmap.getHeight() - 1);
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private Texture createCleaningRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.CLEANING.getColor());
        pixmap.fillRectangle(1, 1, pixmap.getWidth() - 1, pixmap.getHeight() - 1);
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private Texture createRoomConstructionTexture() {
        return AssetsUtil.resizeTexture("build.png", CELL_SIZE, CELL_SIZE);
    }

    private Texture createGrass1Texture() {
        return AssetsUtil.resizeTexture("grass.png", CELL_SIZE, CELL_SIZE);
    }

    private Texture createGrass2Texture() {
        return AssetsUtil.resizeTexture("grass2.png", CELL_SIZE, CELL_SIZE);
    }

    public Texture getActiveEmptyCellTexture() {
        return ACTIVE_EMPTY_CELL;
    }

    public Texture getHallRoomTexture() {
        return HALL_ROOM;
    }

    public Texture getSecurityRoomTexture() {
        return SECURITY_ROOM;
    }

    public Texture getOfficeRoomTexture() {
        return OFFICE_ROOM;
    }

    public Texture getCleaningRoomTexture() {
        return CLEANING_ROOM;
    }

    public Texture getRoomConstructionTexture() {
        return ROOM_CONSTRUCTION;
    }

    public Texture getGrass1Texture() {
        return GRASS1;
    }

    public Texture getGrass2Texture() {
        return GRASS2;
    }

    public Texture getBackground() {
        return background;
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    @Override
    public void dispose() {
        this.ACTIVE_EMPTY_CELL.dispose();
        this.HALL_ROOM.dispose();
        this.SECURITY_ROOM.dispose();
        this.CLEANING_ROOM.dispose();
        this.OFFICE_ROOM.dispose();
        this.ROOM_CONSTRUCTION.dispose();
        this.GRASS1.dispose();
        this.GRASS2.dispose();
        if (this.background != null) {
            this.background.dispose();
        }
    }

}
