package com.tohant.om2d.actor.man;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.model.man.ManInfo;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.GameActorFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.tohant.om2d.service.CommonService.*;

@Getter
@Setter
public abstract class Staff extends Actor {

    private static final float speed = 1.0f;
    private static final String DEFAULT_NAME = "John Silvia";
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    private final String fullName;
    private float salary;
    private final Queue<List<ObjectCell>> pathsQueue;
    private ListIterator<ObjectCell> currentPath;
    private final Texture texture;
    private float speedDelta;

    private final ManInfo manInfo;

    public Staff(String id, float salary) {
        setName(id);
        this.manInfo = new ManInfo(100.0f, 100.0f);
        this.fullName = DEFAULT_NAME;
        this.salary = salary;
        this.pathsQueue = new ConcurrentLinkedQueue<>();
        this.currentPath = null;
        this.texture = AssetService.MAN_STAND;
        debug();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!pathsQueue.isEmpty()) {
            if (currentPath == null) {
                currentPath = pathsQueue.poll().listIterator();
            }
        }
        if (currentPath != null && currentPath.hasNext()) {
            ObjectCell nextCell = currentPath.next();
            if (Float.compare(speedDelta, speed) >= 0) {
                speedDelta = 0.0f;
            } else {
                speedDelta += Gdx.graphics.getDeltaTime();
            }
            setPosition(nextCell.getX(), nextCell.getY());
        } else {
            currentPath = null;
        }

        batch.draw(texture, getX(), getY());
    }

    public abstract Type getType();

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        SECURITY(1200.0f),
        WORKER(0.0f),
        CLEANING(500.0f),
        ADMINISTRATION(1500.0f),
        CAFFE(850.0f);

        private final float salary;

    }

}
