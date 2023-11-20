package com.tohant.om2d.actor.environment;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.service.AssetService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Car extends Actor {

    private final Type type;
    private final CarPath path;
    private final Type.Direction direction;

    public Car(String id, Car.Type type, Type.Direction direction, CarPath path) {
        setName(id);
        setPosition(path.getXStart(), path.getYStart());
        this.type = type;
        this.path = path;
        this.direction = direction;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetService.getCarByTypeAndDirection(this.type, this.direction), getX(), getY());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {

        RED("car.png", "car_h.png");
        private final String pathV;
        private final String pathH;

        public enum Direction {
            BOTTOM, TOP, LEFT, RIGHT
        }
    }

}
