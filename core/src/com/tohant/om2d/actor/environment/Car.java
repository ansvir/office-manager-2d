package com.tohant.om2d.actor.environment;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.service.AssetService;

public class Car extends Actor {

    private final Type type;
    private final CarPath path;
    private final Type.Direction direction;
    private final AssetService assetService;

    public Car(String id, Car.Type type, Type.Direction direction, CarPath path) {
        setName(id);
        setPosition(path.getxStart(), path.getyStart());
        this.type = type;
        this.path = path;
        this.direction = direction;
        this.assetService = AssetService.getInstance();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(assetService.getCarByTypeAndDirection(this.type, this.direction), getX(), getY());
    }

    public Type getType() {
        return type;
    }

    public enum Type {

        RED("car.png", "car_h.png");
        private final String pathV;
        private final String pathH;

        Type(String pathV, String pathH) {
            this.pathV = pathV;
            this.pathH = pathH;
        }

        public String getPathV() {
            return pathV;
        }

        public String getPathH() {
            return pathH;
        }

        public enum Direction {
            BOTTOM, TOP, LEFT, RIGHT
        }
    }

    public CarPath getPath() {
        return path;
    }

    public Type.Direction getDirection() {
        return direction;
    }

}
