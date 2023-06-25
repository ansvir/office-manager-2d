package com.tohant.om2d.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.actor.environment.Car;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.util.AssetsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.tohant.om2d.actor.constant.Constant.*;

public class AssetService implements Disposable {

    private static AssetService instance;

    private final Texture ACTIVE_EMPTY_CELL;
    private final Texture ACTIVE_OBJECT_CELL;
    private final Texture OBJECT_CELL_BORDERS;
    private final Texture HALL_ROOM;
    private final Texture SECURITY_ROOM;
    private final Texture OFFICE_ROOM;
    private final Texture CLEANING_ROOM;
    private final Texture CAFFE_ROOM;
    private final Texture ELEVATOR_ROOM;
    private final Texture ROOM_CONSTRUCTION;
    private final Texture GRASS1;
    private final Texture GRASS2;
    private final Texture MAN_STAND;
    private Texture background;
    private final TextureRegion VR_ROAD;
    private final TextureRegion VL_ROAD;
    private final TextureRegion HU_ROAD;
    private final TextureRegion HD_ROAD;
    private final TextureRegion EMPTY_ROAD;
    private final Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> CARS_TEXTURES;
    private final Cursor DEFAULT_CURSOR;
    private final Cursor.SystemCursor PICK_UP_CURSOR;
    private final Cursor.SystemCursor MOVE_CURSOR;
    private final Pixmap DEFAULT_CURSOR_PIXMAP;
    private final Items items;
    private final Music BG_MUSIC;
    private final Sound NOTIFICATION_SOUND;
    private final Sound CONSTRUCTION_SOUND;
    private final Sound DEMOLISH_SOUND;
    private final Sound CHOOSE_SOUND;
    private final Music MENU_SCREEN_BG_MUSIC;

    private AssetService() {
        this.ACTIVE_EMPTY_CELL = createActiveEmptyCellTexture();
        this.ACTIVE_OBJECT_CELL = createActiveObjectCellTexture();
        this.OBJECT_CELL_BORDERS = createObjectCellBordersTexture();
        this.HALL_ROOM = createHallRoomTexture();
        this.SECURITY_ROOM = createSecurityRoomTexture();
        this.OFFICE_ROOM = createOfficeRoomTexture();
        this.CLEANING_ROOM = createCleaningRoomTexture();
        this.CAFFE_ROOM = createCaffeRoomTexture();
        this.ELEVATOR_ROOM = craeteElevatorRoomTexture();
        this.ROOM_CONSTRUCTION = createRoomConstructionTexture();
        this.GRASS1 = createGrass1Texture();
        this.GRASS2 = createGrass2Texture();
        this.MAN_STAND = createManStandTexture();
        this.VR_ROAD = createVRRoadTexture();
        this.VL_ROAD = createVLRoadTexture();
        this.HD_ROAD = createHDRoadTexture();
        this.HU_ROAD = createHURoadTexture();
        this.EMPTY_ROAD = createEmptyRoadTexture();
        this.CARS_TEXTURES = createCarsTextures();
        this.DEFAULT_CURSOR_PIXMAP = new Pixmap(Gdx.files.internal("cursor.png"));
        this.MOVE_CURSOR = Cursor.SystemCursor.AllResize;
        this.PICK_UP_CURSOR = Cursor.SystemCursor.Crosshair;
        this.DEFAULT_CURSOR = createDefaultCursor();
        this.items = new Items();
        this.BG_MUSIC = createBgMusic();
        this.NOTIFICATION_SOUND = createNotificationSound();
        this.CONSTRUCTION_SOUND = createConstructionSound();
        this.DEMOLISH_SOUND = createDemolishSound();
        this.CHOOSE_SOUND = createChooseSound();
        this.MENU_SCREEN_BG_MUSIC = createMenuScreenBgMusic();
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

    private Texture createObjectCellBordersTexture() {
        Pixmap pixmap = new Pixmap(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, Pixmap.Format.RGBA8888);
        Color borderColor = new Color(Color.GRAY);
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(1, 1, OBJECT_CELL_SIZE - 1, OBJECT_CELL_SIZE - 1);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createActiveObjectCellTexture() {
        Pixmap pixmap = new Pixmap(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, Pixmap.Format.RGBA8888);
        Color borderColor = new Color(Color.BLACK);
        borderColor.a = 0.2f;
        pixmap.setColor(borderColor);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
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
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private Texture createCleaningRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.CLEANING.getColor());
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private Texture createCaffeRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.CAFFE.getColor());
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private Texture craeteElevatorRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.ELEVATOR.getColor());
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
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

    private Texture createManStandTexture() {
        return AssetsUtil.resizeTexture("man_stand.png", OBJECT_CELL_SIZE, OBJECT_CELL_SIZE);
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

    private Cursor createDefaultCursor() {
        return Gdx.graphics.newCursor(DEFAULT_CURSOR_PIXMAP, 0, 0);
    }

    private Music createBgMusic() {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Forest Theme - D Minor - Jordan Ottesen.mp3"));
        music.setLooping(true);
        return music;
    }

    private Sound createNotificationSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/African2.mp3"));
    }

    private Sound createConstructionSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/construction.mp3"));
    }

    private Sound createDemolishSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/demolish.mp3"));
    }

    private Sound createChooseSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/Minimalist3.mp3"));
    }

    private Music createMenuScreenBgMusic() {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Delightful Stroll.mp3"));
        music.setLooping(true);
        return music;
    }

    public Texture getActiveEmptyCellTexture() {
        return ACTIVE_EMPTY_CELL;
    }

    public Texture getActiveObjectCellTexture() {
        return ACTIVE_OBJECT_CELL;
    }

    public Texture getObjectCellBordersTexture() {
        return OBJECT_CELL_BORDERS;
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

    public Texture getCaffeRoomTexture() {
        return CAFFE_ROOM;
    }

    public Texture getElevatorRoomTexture() {
        return ELEVATOR_ROOM;
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
    public Texture getManStandTexture() {
        return MAN_STAND;
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

    public Cursor getDefaultCursor() {
        return DEFAULT_CURSOR;
    }

    public void setCursor(GameCursor gameCursor) {
        if (gameCursor == GameCursor.MAIN) {
            Gdx.graphics.setCursor(DEFAULT_CURSOR);
        } else {
            Gdx.graphics.setSystemCursor(gameCursor == GameCursor.MOVE_CURSOR ? MOVE_CURSOR : PICK_UP_CURSOR);
        }
    }

    public Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> getCarsTextures() {
        return CARS_TEXTURES;
    }

    public TextureRegion getCarByTypeAndDirection(Car.Type type, Car.Type.Direction direction) {
        return CARS_TEXTURES.get(type).get(direction);
    }

    public Music getBgMusic() {
        return BG_MUSIC;
    }

    public Sound getNotificationSound() {
        return NOTIFICATION_SOUND;
    }

    public Sound getConstructionSound() {
        return CONSTRUCTION_SOUND;
    }

    public Sound getDemolishSound() {
        return DEMOLISH_SOUND;
    }

    public Sound getChooseSound() {
        return CHOOSE_SOUND;
    }

    public Music getMenuScreenBgMusic() {
        return MENU_SCREEN_BG_MUSIC;
    }

    public enum GameCursor {
        MAIN, MOVE_CURSOR, PICK_UP
    }

    public static class Items {

        private final Texture PLANT;
        private final Texture COOLER;
        private final Texture PLANT_CELL;
        private final Texture COOLER_CELL;

        public Items() {
            this.PLANT = createPlantTexture();
            this.COOLER = createCoolerTexture();
            this.PLANT_CELL = createPlantCellTexture();
            this.COOLER_CELL = createCoolerCellTexture();
        }

        private Texture createPlantTexture() {
            Pixmap pixmap = new Pixmap((int) TEXTURE_SIZE, (int) TEXTURE_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.GREEN);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture result = new Texture(pixmap);
            pixmap.dispose();
            return result;
        }

        private Texture createCoolerTexture() {
            Pixmap pixmap = new Pixmap((int) TEXTURE_SIZE, (int) TEXTURE_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLACK);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture result = new Texture(pixmap);
            pixmap.dispose();
            return result;
        }

        private Texture createPlantCellTexture() {
            Pixmap pixmap = new Pixmap(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.GREEN);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture result = new Texture(pixmap);
            pixmap.dispose();
            return result;
        }

        private Texture createCoolerCellTexture() {
            Pixmap pixmap = new Pixmap(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLACK);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture result = new Texture(pixmap);
            pixmap.dispose();
            return result;
        }

        public Texture getPlantTexture() {
            return PLANT;
        }

        public Texture getCoolerTexture() {
            return COOLER;
        }

        public Texture getPlantCellTexture() {
            return PLANT_CELL;
        }

        public Texture getCoolerCellTexture() {
            return COOLER_CELL;
        }

    }

    public Items getItems() {
        return items;
    }

    @Override
    public void dispose() {
        this.ACTIVE_EMPTY_CELL.dispose();
        this.ACTIVE_OBJECT_CELL.dispose();
        this.OBJECT_CELL_BORDERS.dispose();
        this.HALL_ROOM.dispose();
        this.SECURITY_ROOM.dispose();
        this.CLEANING_ROOM.dispose();
        this.OFFICE_ROOM.dispose();
        this.CAFFE_ROOM.dispose();
        this.ELEVATOR_ROOM.dispose();
        this.ROOM_CONSTRUCTION.dispose();
        this.GRASS1.dispose();
        this.GRASS2.dispose();
        this.MAN_STAND.dispose();
        this.items.PLANT.dispose();
        this.items.COOLER.dispose();
        this.items.PLANT_CELL.dispose();
        this.items.COOLER_CELL.dispose();
        this.BG_MUSIC.dispose();
        this.NOTIFICATION_SOUND.dispose();
        this.CONSTRUCTION_SOUND.dispose();
        this.DEMOLISH_SOUND.dispose();
        if (this.background != null) {
            this.background.dispose();
        }
    }

}
