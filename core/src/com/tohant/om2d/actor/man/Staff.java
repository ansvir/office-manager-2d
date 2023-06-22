package com.tohant.om2d.actor.man;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.model.man.ManInfo;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.UiActorService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.tohant.om2d.service.ServiceUtil.getObjectCellCoordinates;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.OBJECT_CELL;

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
        this.texture = AssetService.getInstance().getManStandTexture();
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

    public void addPath(ObjectCell source, ObjectCell destination) {
        this.pathsQueue.add(findPath(source, destination));
    }

    public abstract Type getType();

    public enum Type {
        SECURITY(1200.0f),
        WORKER(0.0f),
        CLEANING(500.0f),
        ADMINISTRATION(1500.0f),
        CAFFE(850.0f);

        private final float salary;

        Type(float salary) {
            this.salary = salary;
        }

        public float getSalary() {
            return salary;
        }

    }

    public String getFullName() {
        return fullName;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    private List<ObjectCell> findPath(ObjectCell source, ObjectCell destination) {

        if (source == null || destination == null) {
            return null;
        }

        Queue<ObjectCell> queue = new LinkedList<>();
        Set<ObjectCell> visited = new HashSet<>();
        Map<ObjectCell, ObjectCell> parents = new HashMap<>();

        queue.offer(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            ObjectCell currentCell = queue.poll();

            if (currentCell == destination) {
                List<ObjectCell> path = new ArrayList<>();
                ObjectCell position = destination;
                while (position != source) {
                    path.add(position);
                    position = parents.get(position);
                }
                path.add(source);
                Collections.reverse(path);
                return path;
            }

            for (int[] direction : DIRECTIONS) {
                ObjectCell neighborCell = getNeighborCellId(currentCell, direction);

                if (neighborCell != null && !visited.contains(neighborCell) && !neighborCell.isObstacle()) {
                    queue.offer(neighborCell);
                    visited.add(neighborCell);
                    parents.put(neighborCell, currentCell);
                }
            }
        }

        return null;
    }

    private ObjectCell getNeighborCellId(ObjectCell cell, int[] direction) {
        Vector3 cellCoords = getObjectCellCoordinates(cell);

        int newRow = (int) cellCoords.x + direction[0];
        int newColumn = (int) cellCoords.y + direction[1];

        String cellId = cell.getName().substring(cell.getName().lastIndexOf("_") + 1);
        String neighborCellId = OBJECT_CELL.name() + "#" + newRow + "#" + newColumn + "_" + cellId;
        return (ObjectCell) UiActorService.getInstance().getActorById(neighborCellId);
    }

    public ManInfo getManInfo() {
        return manInfo;
    }

}
