package com.tohant.om2d.service;

import com.badlogic.gdx.Gdx;
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
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.util.AssetsUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.tohant.om2d.actor.constant.Constant.*;

public class AssetService implements Disposable {
    public static final Texture ACTIVE_EMPTY_CELL = createActiveEmptyCellTexture();
    public static final Texture ACTIVE_OBJECT_CELL = createActiveObjectCellTexture();
    public static final Texture OBJECT_CELL_BORDERS = createObjectCellBordersTexture();
    public static final Texture HALL_ROOM = createHallRoomTexture();
    public static final Texture SECURITY_ROOM = createSecurityRoomTexture();
    public static final Texture OFFICE_ROOM = createOfficeRoomTexture();
    public static final Texture CLEANING_ROOM = createCleaningRoomTexture();
    public static final Texture CAFFE_ROOM = createCaffeRoomTexture();
    public static final Texture ELEVATOR_ROOM = craeteElevatorRoomTexture();
    public static final Texture ROOM_CONSTRUCTION = createRoomConstructionTexture();
    public static final Texture GRASS1 = createGrass1Texture();
    public static final Texture GRASS2 = createGrass2Texture();
    public static final Texture MAN_STAND = createManStandTexture();
    public static final TextureRegion VR_ROAD = createVRRoadTexture();
    public static final TextureRegion VL_ROAD = createVLRoadTexture();
    public static final TextureRegion HD_ROAD = createHDRoadTexture();
    public static final TextureRegion HU_ROAD = createHURoadTexture();
    public static final TextureRegion EMPTY_ROAD = createEmptyRoadTexture();
    public static final Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> CARS_TEXTURES = createCarsTextures();
    public static final Pixmap DEFAULT_CURSOR_PIXMAP = new Pixmap(Gdx.files.internal("cursor.png"));
    public static final Cursor.SystemCursor MOVE_CURSOR = Cursor.SystemCursor.AllResize;
    public static final Cursor.SystemCursor PICK_UP_CURSOR = Cursor.SystemCursor.Crosshair;
    public static final Cursor DEFAULT_CURSOR = createDefaultCursor();
    public static final Music BG_MUSIC = createBgMusic();
    public static Texture BACKGROUND;
    public static final Sound NOTIFICATION_SOUND = createNotificationSound();
    public static final Sound CONSTRUCTION_SOUND = createConstructionSound();
    public static final Sound DEMOLISH_SOUND = createDemolishSound();
    public static final Sound CHOOSE_SOUND = createChooseSound();
    public static final Music MENU_SCREEN_BG_MUSIC = createMenuScreenBgMusic();
    public static final Texture WORLD_MAP = createWorldMapTexture();
    public static final Texture WORLD_MAP_BG = createWorldMapBgTexture();

    private static Texture createActiveEmptyCellTexture() {
        Pixmap cellPixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        Color bordersColor = Color.BLACK;
        bordersColor.a = 0.2f;
        cellPixmap.setColor(bordersColor);
        cellPixmap.fill();
        Texture result = new Texture(cellPixmap);
        cellPixmap.dispose();
        return result;
    }

    private static Texture createObjectCellBordersTexture() {
        Pixmap pixmap = new Pixmap(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, Pixmap.Format.RGBA8888);
        Color borderColor = new Color(Color.GRAY);
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(1, 1, OBJECT_CELL_SIZE - 1, OBJECT_CELL_SIZE - 1);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private static Texture createActiveObjectCellTexture() {
        Pixmap pixmap = new Pixmap(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, Pixmap.Format.RGBA8888);
        Color borderColor = new Color(Color.BLACK);
        borderColor.a = 0.2f;
        pixmap.setColor(borderColor);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private static Texture createHallRoomTexture() {
        return AssetsUtil.resizeTexture("hall.png", CELL_SIZE, CELL_SIZE);
    }

    private static Texture createSecurityRoomTexture() {
        return AssetsUtil.resizeTexture("security.png", CELL_SIZE, CELL_SIZE);
    }

    private static Texture createOfficeRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.OFFICE.getColor());
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private static Texture createCleaningRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.CLEANING.getColor());
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private static Texture createCaffeRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.CAFFE.getColor());
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private static Texture craeteElevatorRoomTexture() {
        Pixmap pixmap = new Pixmap(CELL_SIZE, CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Room.Type.ELEVATOR.getColor());
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    private static Texture createRoomConstructionTexture() {
        return AssetsUtil.resizeTexture("build.png", CELL_SIZE, CELL_SIZE);
    }

    private static Texture createGrass1Texture() {
        return AssetsUtil.resizeTexture("grass.png", CELL_SIZE, CELL_SIZE);
    }

    private static Texture createGrass2Texture() {
        return AssetsUtil.resizeTexture("grass2.png", CELL_SIZE, CELL_SIZE);
    }

    private static Texture createManStandTexture() {
        return AssetsUtil.resizeTexture("man_stand.png", OBJECT_CELL_SIZE, OBJECT_CELL_SIZE);
    }

    private static TextureRegion createVRRoadTexture() {
        return new TextureRegion(AssetsUtil.resizeTexture("road.png", CELL_SIZE, CELL_SIZE));
    }

    private static TextureRegion createVLRoadTexture() {
        TextureRegion texture = new TextureRegion(Objects.requireNonNullElseGet(VR_ROAD, AssetService::createVRRoadTexture));
        texture.flip(true, false);
        return texture;
    }

    private static TextureRegion createHURoadTexture() {
        TextureRegion texture = new TextureRegion(Objects.requireNonNullElseGet(HD_ROAD, AssetService::createHDRoadTexture));
        texture.flip(false, true);
        return texture;
    }

    private static TextureRegion createHDRoadTexture() {
        return new TextureRegion(AssetsUtil.resizeTexture("road_h.png", CELL_SIZE, CELL_SIZE));
    }

    private static TextureRegion createEmptyRoadTexture() {
        return new TextureRegion(AssetsUtil.resizeTexture("empty_road.png", CELL_SIZE, CELL_SIZE));
    }

    private static Map<Car.Type, Map<Car.Type.Direction, TextureRegion>> createCarsTextures() {
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

    private static Texture createWorldMapTexture() {
        return new Texture(Gdx.files.internal("world_map.jpg"));
    }

    private static Texture createWorldMapBgTexture() {
        return new Texture(Gdx.files.internal("world_map_bg.jpg"));
    }

    private static Cursor createDefaultCursor() {
        return Gdx.graphics.newCursor(DEFAULT_CURSOR_PIXMAP, 0, 0);
    }

    private static Music createBgMusic() {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Forest Theme - D Minor - Jordan Ottesen.mp3"));
        music.setLooping(true);
        return music;
    }

    private static Sound createNotificationSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/African2.mp3"));
    }

    private static Sound createConstructionSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/construction.mp3"));
    }

    private static Sound createDemolishSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/demolish.mp3"));
    }

    private static Sound createChooseSound() {
        return Gdx.audio.newSound(Gdx.files.internal("audio/Minimalist3.mp3"));
    }

    private static Music createMenuScreenBgMusic() {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Delightful Stroll.mp3"));
        music.setLooping(true);
        return music;
    }

    public static void setCursor(GameCursor gameCursor) {
        if (gameCursor == GameCursor.MAIN) {
            Gdx.graphics.setCursor(DEFAULT_CURSOR);
        } else {
            Gdx.graphics.setSystemCursor(gameCursor == GameCursor.MOVE_CURSOR ? MOVE_CURSOR : PICK_UP_CURSOR);
        }
    }

    public static TextureRegion getCarByTypeAndDirection(Car.Type type, Car.Type.Direction direction) {
        return CARS_TEXTURES.get(type).get(direction);
    }

    public enum GameCursor {
        MAIN, MOVE_CURSOR, PICK_UP
    }

    public static class Items {

        public static final Texture PLANT = createPlantTexture();
        public static Texture COOLER = createCoolerTexture();
        public static Texture PLANT_CELL = createPlantCellTexture();
        public static Texture COOLER_CELL = createCoolerCellTexture();

        private static Texture createPlantTexture() {
            return new Texture(Gdx.files.internal("plant.png"));
        }

        private static Texture createCoolerTexture() {
            Pixmap pixmap = new Pixmap((int) TEXTURE_SIZE, (int) TEXTURE_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLACK);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture result = new Texture(pixmap);
            pixmap.dispose();
            return result;
        }

        private static Texture createPlantCellTexture() {
            return AssetsUtil.resizeTexture("plant.png", OBJECT_CELL_SIZE, OBJECT_CELL_SIZE);
        }

        private static Texture createCoolerCellTexture() {
            Pixmap pixmap = new Pixmap(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLACK);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture result = new Texture(pixmap);
            pixmap.dispose();
            return result;
        }

    }

    public static class Character {
        public static final Texture BODY_FAT_DARK = createBodyFatDarkTexture();
        public static final Texture BODY_FAT_LIGHT = createBodyFatLightTexture();
        public static final Texture BODY_FAT_MEDIUM = createBodyFatMediumTexture();
        public static final Texture BODY_MIDDLE_DARK = createBodyMiddleDarkTexture();
        public static final Texture BODY_MIDDLE_LIGHT = createBodyMiddleLightTexture();
        public static final Texture BODY_MIDDLE_MEDIUM = createBodyMiddleMediumTexture();
        public static final Texture BODY_THIN_DARK = createBodyThinDarkTexture();
        public static final Texture BODY_THIN_LIGHT = createBodyThinLightTexture();
        public static final Texture BODY_THIN_MEDIUM = createBodyThinMediumTexture();
        public static final Texture HAIR_ICON_BLOND = createHairIconBlondTexture();
        public static final Texture HAIR_ICON_DARK = createHairIconDarkTexture();
        public static final Texture HAIR_ICON_BROWN = createHairIconBrownTexture();
        public static final Texture HAIR_ICON_GRAY = createHairIconGrayTexture();
        public static final Texture HEAD_ICON_DARK = createHeadIconDarkTexture();
        public static final Texture HEAD_ICON_LIGHT = createHeadIconLightTexture();
        public static final Texture HEAD_ICON_MEDIUM = createHeadIconMediumTexture();

        private static Texture createHeadIconMediumTexture() {
            return new Texture(Gdx.files.internal("textures/man/head_icon_medium.png"));
        }

        private static Texture createHeadIconLightTexture() {
            return new Texture(Gdx.files.internal("textures/man/head_icon_light.png"));
        }

        private static Texture createHeadIconDarkTexture() {
            return new Texture(Gdx.files.internal("textures/man/head_icon_dark.png"));
        }

        private static Texture createHairIconGrayTexture() {
            return new Texture(Gdx.files.internal("textures/man/hair_icon_gray.png"));
        }

        private static Texture createHairIconDarkTexture() {
            return new Texture(Gdx.files.internal("textures/man/hair_icon_dark.png"));
        }

        private static Texture createHairIconBrownTexture() {
            return new Texture(Gdx.files.internal("textures/man/hair_icon_brown.png"));
        }

        private static Texture createHairIconBlondTexture() {
            return new Texture(Gdx.files.internal("textures/man/hair_icon_blond.png"));
        }

        private static Texture createBodyThinMediumTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_thin_medium.png"));
        }

        private static Texture createBodyThinLightTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_thin_light.png"));
        }

        private static Texture createBodyThinDarkTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_thin_dark.png"));
        }

        private static Texture createBodyMiddleMediumTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_middle_medium.png"));
        }

        private static Texture createBodyMiddleLightTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_middle_light.png"));
        }

        private static Texture createBodyMiddleDarkTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_middle_dark.png"));
        }

        private static Texture createBodyFatMediumTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_fat_medium.png"));
        }

        private static Texture createBodyFatLightTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_fat_light.png"));
        }

        private static Texture createBodyFatDarkTexture() {
            return new Texture(Gdx.files.internal("textures/man/body_fat_dark.png"));
        }

        public enum Type {
            FAT_DARK(new Texture[]{
                    HEAD_ICON_DARK,
                    BODY_FAT_DARK
            }),
            FAT_MEDIUM(new Texture[]{
                    HEAD_ICON_MEDIUM,
                    BODY_FAT_MEDIUM
            }),
            FAT_LIGHT(new Texture[]{
                    HEAD_ICON_LIGHT,
                    BODY_FAT_LIGHT
            }),
            MIDDLE_DARK(new Texture[]{
                    HEAD_ICON_DARK,
                    BODY_MIDDLE_DARK
            }),
            MIDDLE_MEDIUM(new Texture[]{
                    HEAD_ICON_MEDIUM,
                    BODY_MIDDLE_MEDIUM
            }),
            MIDDLE_LIGHT(new Texture[]{
                    HEAD_ICON_LIGHT,
                    BODY_MIDDLE_LIGHT
            }),
            THIN_DARK(new Texture[]{
                    HEAD_ICON_DARK,
                    BODY_THIN_DARK
            }),
            THIN_MEDIUM(new Texture[]{
                    HEAD_ICON_MEDIUM,
                    BODY_THIN_MEDIUM
            }),
            THIN_LIGHT(new Texture[]{
                    HEAD_ICON_LIGHT,
                    BODY_THIN_LIGHT
            });

            @Getter
            private final Texture texture;
            private final Texture[] textures;

            Type(Texture[] textures) {
                this.textures = textures;
                this.texture = createTexture();
            }

            private Texture createTexture() {
                Pixmap result = new Pixmap(42, 134 + 30, Pixmap.Format.RGBA8888);
                if (!this.textures[0].getTextureData().isPrepared()) {
                    this.textures[0].getTextureData().prepare();
                }
                if (!this.textures[1].getTextureData().isPrepared()) {
                    this.textures[1].getTextureData().prepare();
                }
                result.drawPixmap(this.textures[1].getTextureData().consumePixmap(), 0, 0);
                result.drawPixmap(this.textures[0].getTextureData().consumePixmap(), 42 / 2 - this.textures[0].getWidth() / 2, 2);
                return new Texture(result);
            }

        }

    }

    @Override
    public void dispose() {
        ACTIVE_EMPTY_CELL.dispose();
        ACTIVE_OBJECT_CELL.dispose();
        OBJECT_CELL_BORDERS.dispose();
        HALL_ROOM.dispose();
        SECURITY_ROOM.dispose();
        CLEANING_ROOM.dispose();
        OFFICE_ROOM.dispose();
        CAFFE_ROOM.dispose();
        ELEVATOR_ROOM.dispose();
        ROOM_CONSTRUCTION.dispose();
        GRASS1.dispose();
        GRASS2.dispose();
        MAN_STAND.dispose();
        Items.PLANT.dispose();
        Items.COOLER.dispose();
        Items.PLANT_CELL.dispose();
        Items.COOLER_CELL.dispose();
        Character.BODY_FAT_DARK.dispose();
        Character.BODY_FAT_LIGHT.dispose();
        Character.BODY_FAT_MEDIUM.dispose();
        Character.BODY_MIDDLE_DARK.dispose();
        Character.BODY_MIDDLE_LIGHT.dispose();
        Character.BODY_MIDDLE_MEDIUM.dispose();
        Character.HAIR_ICON_BLOND.dispose();
        Character.HAIR_ICON_DARK.dispose();
        Character.HAIR_ICON_GRAY.dispose();
        Character.HEAD_ICON_DARK.dispose();
        Character.HEAD_ICON_LIGHT.dispose();
        Character.HEAD_ICON_MEDIUM.dispose();
        BG_MUSIC.dispose();
        NOTIFICATION_SOUND.dispose();
        CONSTRUCTION_SOUND.dispose();
        DEMOLISH_SOUND.dispose();
        WORLD_MAP.dispose();
        WORLD_MAP_BG.dispose();
    }

}
