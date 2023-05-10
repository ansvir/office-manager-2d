package com.tohant.om2d.service;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.actor.environment.Car;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.util.AssetsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private final TextureRegion VR_ROAD;
    private final TextureRegion VL_ROAD;
    private final TextureRegion HU_ROAD;
    private final TextureRegion HD_ROAD;
    private final TextureRegion EMPTY_ROAD;
    private final Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> CARS_TEXTURES;

    private AssetService() {
        this.ACTIVE_EMPTY_CELL = createActiveEmptyCellTexture();
        this.HALL_ROOM = createHallRoomTexture();
        this.SECURITY_ROOM = createSecurityRoomTexture();
        this.OFFICE_ROOM = createOfficeRoomTexture();
        this.CLEANING_ROOM = createCleaningRoomTexture();
        this.ROOM_CONSTRUCTION = createRoomConstructionTexture();
        this.GRASS1 = createGrass1Texture();
        this.GRASS2 = createGrass2Texture();
        this.VR_ROAD = createVRRoadTexture();
        this.VL_ROAD = createVLRoadTexture();
        this.HD_ROAD = createHDRoadTexture();
        this.HU_ROAD = createHURoadTexture();
        this.EMPTY_ROAD = createEmptyRoadTexture();
        this.CARS_TEXTURES = createCarsTextures();
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

    private TextureRegion createVRRoadTexture() {
        return new TextureRegion(AssetsUtil.resizeTexture("road.png", CELL_SIZE, CELL_SIZE));
    }

    private TextureRegion createVLRoadTexture() {
        TextureRegion texture = new TextureRegion(Objects.requireNonNullElseGet(VR_ROAD, this::createVRRoadTexture));
        texture.flip(true, false);
        return texture;
    }

    private TextureRegion createHURoadTexture() {
        TextureRegion texture = new TextureRegion(Objects.requireNonNullElseGet(HD_ROAD, this::createHDRoadTexture));
        texture.flip(false, true);
        return texture;
    }

    private TextureRegion createHDRoadTexture() {
        return new TextureRegion(AssetsUtil.resizeTexture("road_h.png", CELL_SIZE, CELL_SIZE));
    }

    private TextureRegion createEmptyRoadTexture() {
        return new TextureRegion(AssetsUtil.resizeTexture("empty_road.png", CELL_SIZE, CELL_SIZE));
    }

    private Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> createCarsTextures() {
        Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> regions = new HashMap<>();
        for (Car.Type t : Car.Type.values()) {
            Texture bottomTexture = new Texture(t.getPathV());
            TextureRegion bottom = new TextureRegion(bottomTexture);
            Texture topTexture = new Texture(t.getPathV());
            TextureRegion top = new TextureRegion(topTexture);
            top.flip(false, true);
            Texture leftTexture = new Texture(t.getPathH());
            TextureRegion left = new TextureRegion(leftTexture);
            left.flip(true, false);
            Texture rightTexture = new Texture(t.getPathH());
            TextureRegion right = new TextureRegion(rightTexture);
            Map<Car.Type.Direction, TextureRegion> directions = Map.of(Car.Type.Direction.LEFT, left,
                    Car.Type.Direction.RIGHT, right, Car.Type.Direction.TOP, top, Car.Type.Direction.BOTTOM, bottom);
            regions.put(t, directions);
        }
        return regions;
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

    public TextureRegion getVRRoadTexture() {
        return VR_ROAD;
    }

    public TextureRegion getVLRoadTexture() {
        return VL_ROAD;
    }

    public TextureRegion getHURoadTexture() {
        return HU_ROAD;
    }

    public TextureRegion getHDRoadTexture() {
        return HD_ROAD;
    }

    public TextureRegion getEmptyRoadTexture() {
        return EMPTY_ROAD;
    }

    public Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> getCarsTextures() {
        return CARS_TEXTURES;
    }

    public TextureRegion getCarByTypeAndDirection(Car.Type type, Car.Type.Direction direction) {
        return CARS_TEXTURES.get(type).get(direction);
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
