package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.tohant.om2d.actor.environment.Car;
import com.tohant.om2d.actor.environment.CarPath;
import com.tohant.om2d.actor.environment.Road;
import com.tohant.om2d.model.RoadType;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;

import java.util.concurrent.atomic.AtomicReference;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.actor.environment.Car.Type.Direction.BOTTOM;
import static com.tohant.om2d.actor.environment.Car.Type.Direction.TOP;
import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.CAR;
import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.ROAD;

public class Background extends Group {

    private final Array<Road> roads;
    private final Array<Car> cars;
    private final TimeLineTask<Boolean> backgroundTimeline;
    private final AsyncExecutor executor;
    private float timePassed;
    private final GameActorSearchService gameActorSearchService;

    public Background(String id, float width, float height, GameActorSearchService gameActorSearchService) {
        setName(id);
        setSize(width, height);
        roads = new Array<>();
        cars = new Array<>();
        createRoads();
        backgroundTimeline = new TimeLineTask<>(DAY_WAIT_TIME_MILLIS / 2L, () -> {}, true);
        executor = new AsyncExecutor(1);
        executor.submit(backgroundTimeline);
        this.gameActorSearchService = gameActorSearchService;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (timePassed * 1000L >= (float) DAY_WAIT_TIME_MILLIS / 2L) {
            timePassed = 0.0f;
        } else {
            timePassed += Gdx.graphics.getDeltaTime();
        }
        updateCars();
        Texture bg = AssetService.BACKGROUND;
        if (bg == null) {
            bg = createBackground();
            AssetService.BACKGROUND = bg;
        }
        batch.draw(bg, getX(), getY());
        for (int i = 0; i < roads.size; i++) {
            roads.get(i).draw(batch, parentAlpha);
        }
        for (int i = 0; i < cars.size; i++) {
            cars.get(i).draw(batch, parentAlpha);
        }
    }

    private Texture createBackground() {
        int width = Math.round(getWidth() / CELL_SIZE);
        int height = Math.round(getHeight() / CELL_SIZE);
        Texture grass1 = AssetService.GRASS1;
        Texture grass2 = AssetService.GRASS2;
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
        int roadOneStartPos = width / 5;
        roads.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                String roadId = ROAD.name() + COORD_DELIMITER + i + COORD_DELIMITER + j;
                if (i == roadOneStartPos) {
                    Road road = new Road(roadId, i * CELL_SIZE, j * CELL_SIZE, RoadType.EMPTY);
                    roads.add(road);
                    addActor(road);
                }
                if (i == roadOneStartPos + 1) {
                    Road road = new Road(roadId, i * CELL_SIZE, j * CELL_SIZE, RoadType.RIGHT);
                    roads.add(road);
                    addActor(road);
                }
                if (i == roadOneStartPos + 2) {
                    Road road = new Road(roadId, i * CELL_SIZE, j * CELL_SIZE, RoadType.LEFT);
                    roads.add(road);
                    addActor(road);
                }
                if (i == roadOneStartPos + 3) {
                    Road road = new Road(roadId, i * CELL_SIZE, j * CELL_SIZE, RoadType.EMPTY);
                    roads.add(road);
                    addActor(road);
                }
            }
        }
    }

    private synchronized void updateCars() {
        Array<Car> carsToRemove = new Array<>();
        for (int i = 0; i < cars.size; i++) {
            if (cars.get(i).getX() <= getX() || cars.get(i).getY() <= getY()
                    || cars.get(i).getX() >= getX() + getWidth() || cars.get(i).getY() >= getY() + getHeight()) {
                carsToRemove.add(cars.get(i));
            }
        }
        if (!cars.isEmpty()) {
            cars.removeAll(carsToRemove, false);
            getChildren().removeAll(carsToRemove, false);
        }
        trySpawnCar();
        Array<Actor> actors = gameActorSearchService.getActorsByIdPrefix(CAR.name());
        actors.forEach(a -> {
            if (a instanceof Car) {
                if (((Car) a).getDirection() == BOTTOM && a.getY() > -a.getHeight()) {
                    a.setY(a.getY() - 5);
                } else if (((Car) a).getDirection() == Car.Type.Direction.TOP) {
                    a.setY(a.getY() + 5);
                }
            }
        });
    }

    private synchronized void trySpawnCar() {
        if (timePassed * 1000L >= backgroundTimeline.getWaitTime() && (backgroundTimeline.getDate().getDays() == 7
                || backgroundTimeline.getDate().getDays() == 25)) {
            AtomicReference<CarPath> nextCar = new AtomicReference<>();
            roads.forEach(r -> {
                if (r.getType() == RoadType.RIGHT) {
                    nextCar.set(new CarPath((int) r.getX(), (int) getHeight(), (int) r.getX(), (int) getY()));
                }
            });
            if (nextCar.get() != null) {
                Car car = new Car(CAR.name() + COORD_DELIMITER + BOTTOM.name(), Car.Type.RED, BOTTOM, nextCar.get());
                cars.add(car);
                addActor(car);
            }
        } else if (timePassed * 1000L >= backgroundTimeline.getWaitTime() && (backgroundTimeline.getDate().getDays() == 20
                || backgroundTimeline.getDate().getDays() == 3)) {
            AtomicReference<CarPath> nextCar = new AtomicReference<>();
            roads.forEach(r -> {
                if (r.getType() == RoadType.LEFT) {
                    nextCar.set(new CarPath((int) r.getX(), (int) getY(), (int) r.getX(), (int) getHeight()));
                }
            });
            if (nextCar.get() != null) {
                Car car = new Car(CAR.name() + COORD_DELIMITER + TOP.name(), Car.Type.RED, Car.Type.Direction.TOP, nextCar.get());
                cars.add(car);
                addActor(car);
            }
        }
    }

}
