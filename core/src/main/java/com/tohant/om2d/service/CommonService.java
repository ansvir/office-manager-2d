package com.tohant.om2d.service;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.actor.ObjectCellItem;
import com.tohant.om2d.actor.constant.CompanyConstant;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.item.PlaceItemCommand;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.CellDao;
import com.tohant.om2d.storage.database.LevelDao;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.Items;
import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.OBJECT_CELL;
import static com.tohant.om2d.storage.cache.GameCache.*;

/**
 * Utility class for validation and utility methods for mappings between actors names
 * in the database and stages.
 */
@Component
@RequiredArgsConstructor
public class CommonService {

    private final GameCache gameCache;
    private final PlaceItemCommand placeItemCommand;
    private final CellDao cellDao;
    private final LevelDao levelDao;

    public int nextToHalls(Cell cell, GameActorSearchService gameActorSearchService) {
        Array<Cell> neighborCells = getNeighborCells(cell, gameActorSearchService);
        int points = 0;
        for (int i = 0; i < neighborCells.size; i++) {
            if (!neighborCells.get(i).isEmpty() && isBuilt(neighborCells.get(i))) {
                Room room = getCellRoom(neighborCells.get(i));
                if (room != null && room.getType() == Room.Type.HALL) {
                    points++;
                }
            }
        }
        return points;
    }

    public Array<Cell> getNeighborCells(Cell cell, GameActorSearchService gameActorSearchService) {
        LevelEntity levelEntity = levelDao.queryForId(UUID.fromString(gameCache.getValue(GameCache.CURRENT_LEVEL_ID)));
        Array<CellEntity> cellEntities = new Array<>(levelEntity.getCellEntities().toArray(CellEntity[]::new));
        Iterable<CellEntity> neighborCells = cellEntities.select(c -> (c.getX() == cell.getX() - 1 && c.getY() == cell.getY())
        || (c.getX() == cell.getX() + 1 && c.getY() == cell.getY())
        || (c.getX() == cell.getX() && c.getY() == cell.getY() - 1)
        || (c.getX() == cell.getX() && c.getY() == cell.getY() + 1));
        Array<Cell> cells = new Array<>();
        for (CellEntity cellEntity : neighborCells) {
            Actor found = gameActorSearchService.getActorById(cellEntity.getId().toString());
            if (found != null) {
                cells.add((Cell) found);
            }
        }
        return cells;
    }

    public boolean checkHallNextToRoomThatHasNoOtherHalls(Cell hall, GameActorSearchService gameActorSearchService) {
        Array<Cell> cells = getNeighborCells(hall, gameActorSearchService);
        int points = 0;
        for (Cell c : cells) {
            Room room = getCellRoom(c);
            if (room != null && room.getType() != Room.Type.HALL && nextToHalls(c, gameActorSearchService) <= 1) {
                points++;
            }
        }
        return points > 0;
    }

    public Room getCellRoom(Cell cell) {
        Array<Actor> cells = cell.getChildren();
        for (int i = 0 ; i < cells.size; i++) {
            if (cells.get(i) instanceof Room) {
                return (Room) cells.get(i);
            }
        }
        return null;
    }

    public static Vector2 getObjectCellCoordinates(ObjectCell cell) {
        String[] objectsNames = cell.getName().split(ID_DELIMITER);
        String[] coords = objectsNames[0].split(COORD_DELIMITER);
        return new Vector2(Long.parseLong(coords[1]), Long.parseLong(coords[2]));
    }

    public static String getObjectCellCellName(ObjectCell cell) {
        String[] objectsNames = cell.getName().split(ID_DELIMITER);
        String[] coords = objectsNames[0].split(COORD_DELIMITER);
        return coords[3];
    }

    public static Vector2 getObjectCellItemCoordinatesByName(String name) {
        String[] nameAndCoords = name.split(COORD_DELIMITER);
        return new Vector2(Long.parseLong(nameAndCoords[1]), Long.parseLong(nameAndCoords[2]));
    }

    public static String getObjectCellItemId(String itemType, String objectCellId) {
        return itemType + ID_DELIMITER + objectCellId;
    }

    public static String getObjectCellActorId(int objectCellX, int objectCellY, String cellId) {
        return OBJECT_CELL.name() + COORD_DELIMITER + objectCellX + COORD_DELIMITER + objectCellY + ID_DELIMITER + cellId;
    }

    public static String addItemToCell(String items, String item) {
        if (items == null) {
            return item;
        } else {
            return items + ";" + item;
        }
    }

    public static boolean checkNoCellOnGrid(Array<Actor> children) {
        boolean result = true;
        for (int i = 0; i < children.size; i++) {
            Actor a = children.get(i);
            if (a instanceof Cell) {
                if (!((Cell) a).isEmpty()) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public long getEmployeesAmountByType(Staff.Type type) {
        switch (type) {
            case SECURITY:
                return gameCache.getLong(GameCache.TOTAL_SECURITY_STAFF);
            case WORKER:
                return gameCache.getLong(GameCache.TOTAL_WORKERS);
            case CLEANING:
                return gameCache.getLong(GameCache.TOTAL_CLEANING_STAFF);
            case ADMINISTRATION:
                return gameCache.getLong(GameCache.TOTAL_ADMIN_STAFF);
            case CAFFE:
                return gameCache.getLong(GameCache.TOTAL_CAFFE_STAFF);
            default:
                return -1L;
        }
    }

    public void setEmployeesAmountByType(Staff.Type type, long amount) {
        switch (type) {
            case SECURITY:
                gameCache.setLong(GameCache.TOTAL_SECURITY_STAFF, amount);
                break;
            case CLEANING:
                gameCache.setLong(GameCache.TOTAL_CLEANING_STAFF, amount);
                break;
            case WORKER:
                gameCache.setLong(GameCache.TOTAL_WORKERS, amount);
                break;
            case ADMINISTRATION:
                gameCache.setLong(GameCache.TOTAL_ADMIN_STAFF, amount);
                break;
            case CAFFE:
                gameCache.setLong(GameCache.TOTAL_CAFFE_STAFF, amount);
                break;
            default:
                break;
        }
    }

    public long getRoomsAmountByType(Room.Type type) {
        switch (type) {
            case OFFICE:
                return gameCache.getLong(GameCache.OFFICES_AMOUNT);
            case HALL:
                return gameCache.getLong(GameCache.HALLS_AMOUNT);
            case SECURITY:
                return gameCache.getLong(GameCache.SECURITY_AMOUNT);
            case CLEANING:
                return gameCache.getLong(GameCache.CLEANING_AMOUNT);
            case CAFFE:
                return gameCache.getLong(GameCache.CAFFE_AMOUNT);
            case ELEVATOR:
                return gameCache.getLong(GameCache.ELEVATOR_AMOUNT);
            default:
                return -1L;
        }
    }

    public void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE:
                gameCache.setLong(GameCache.OFFICES_AMOUNT, amount);
                break;
            case HALL:
                gameCache.setLong(GameCache.HALLS_AMOUNT, amount);
                break;
            case SECURITY:
                gameCache.setLong(GameCache.SECURITY_AMOUNT, amount);
                break;
            case CLEANING:
                gameCache.setLong(GameCache.CLEANING_AMOUNT, amount);
                break;
            case CAFFE:
                gameCache.setLong(GameCache.CAFFE_AMOUNT, amount);
                break;
            case ELEVATOR:
                gameCache.setLong(GameCache.ELEVATOR_AMOUNT, amount);
            default:
                break;
        }
    }

    public Room.Type getCurrentRoomType() {
        String value = gameCache.getValue(GameCache.CURRENT_ROOM_TYPE);
        if (value == null) {
            return null;
        } else {
            return Room.Type.valueOf(value);
        }
    }

    public static String buildRandomCompanyName() {
        String first = CompanyConstant.values()[MathUtils.random(CompanyConstant.values().length - 1)].name();
        String second = CompanyConstant.values()[MathUtils.random(CompanyConstant.values().length - 1)].name();
        first = first.charAt(0) + first.substring(1).toLowerCase();
        second = second.charAt(0) + second.substring(1).toLowerCase();
        return first + " " + second;
    }

    public Array<Array<ObjectCell>> getObjectCells(Cell cell, Room.Type room) {
        CellEntity cellEntity = cellDao.queryForId(UUID.fromString(cell.getName()));
        Array<Array<ObjectCell>> cells = new Array<>();
        if (room == null) {
            for (int i = 0; i <= OBJECT_CELL_SIZE + 1; i++) {
                cells.insert(i, new Array<>());
                for (int j = 0; j <= OBJECT_CELL_SIZE + 1; j++) {
                    cells.get(i).insert(j, new ObjectCell(getObjectCellActorId(i, j, cellEntity.getId().toString()),
                            i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, false));
                }
            }
        } else {
            switch (room) {
                case HALL: {
                    for (int i = 0; i <= OBJECT_CELL_SIZE + 1; i++) {
                        cells.insert(i, new Array<>());
                        for (int j = 0; j <= OBJECT_CELL_SIZE + 1; j++) {
                            cells.get(i).insert(j, new ObjectCell(getObjectCellActorId(i, j, cellEntity.getId().toString()),
                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, false));
                        }
                    }
                    break;
                }
                default: {
                    for (int i = 0; i <= OBJECT_CELL_SIZE + 1; i++) {
                        cells.insert(i, new Array<>());
                        for (int j = 0; j <= OBJECT_CELL_SIZE + 1; j++) {
                            cells.get(i).insert(j,
                                    i == 0 && j < OBJECT_CELL_SIZE / 2f && j > OBJECT_CELL_SIZE / 2f ?
                                            new ObjectCell(getObjectCellActorId(i, j, cellEntity.getId().toString()),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : j == 0 && i < OBJECT_CELL_SIZE / 2f && i > OBJECT_CELL_SIZE / 2f ?
                                            new ObjectCell(getObjectCellActorId(i, j, cellEntity.getId().toString()),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : i == OBJECT_CELL_SIZE - 1 && j < OBJECT_CELL_SIZE / 2f && j > OBJECT_CELL_SIZE / 2f ?
                                            new ObjectCell(getObjectCellActorId(i, j, cellEntity.getId().toString()),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : j == OBJECT_CELL_SIZE - 1 && i < OBJECT_CELL_SIZE / 2f && i > OBJECT_CELL_SIZE / 2f ?
                                            new ObjectCell(getObjectCellActorId(i, j, cellEntity.getId().toString()),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : new ObjectCell(getObjectCellActorId(i, j, cellEntity.getId().toString()),
                                            i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, false));
                        }
                    }
                    break;
                }
            }
        }
        addObjectCellListener(cells);
        return cells;
    }

    private void addObjectCellListener(Array<Array<ObjectCell>> cells) {
        for (Array<ObjectCell> oss: cells) {
            for (ObjectCell os : oss) {
                os.addListener(new InputListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        super.enter(event, x, y, pointer, fromActor);
                        if (gameCache.getObject(CURRENT_ITEM) != null) {
                            gameCache.setObject(CURRENT_OBJECT_CELL, os);
                            os.setActive(true);
                        }
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        super.exit(event, x, y, pointer, toActor);
                        os.setActive(false);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        if (gameCache.getObject(CURRENT_ITEM) != null) {
                            try {
                                placeItemCommand.execute();
                            } catch (GameException e) {
                                Array<GameException> exceptions = (Array<GameException>) gameCache.getObject(GAME_EXCEPTION);
                                exceptions.add(e);
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    /*
    public ObjectCell getNeighborObjectCell(ObjectCell cell, int[] direction) {
        Vector2 cellCoords = getObjectCellCoordinates(cell);
        String cellId = getObjectCellCellName(cell);
        int newRow = (int) cellCoords.x + direction[0];
        int newColumn = (int) cellCoords.y + direction[1];
        String neighborObjectCellId = getObjectCellActorId(newRow, newColumn, cellId);
        return (ObjectCell) gameActorFactory.getActorById(neighborObjectCellId);
    }

    private List<ObjectCell> findPath(int[][] directions, ObjectCell source, ObjectCell destination) {

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

            for (int[] direction : directions) {
                ObjectCell neighborCell = getNeighborObjectCell(currentCell, direction);

                if (neighborCell != null && !visited.contains(neighborCell) && !neighborCell.isObstacle()) {
                    queue.offer(neighborCell);
                    visited.add(neighborCell);
                    parents.put(neighborCell, currentCell);
                }
            }
        }

        return null;
    }

     */

    public void addObjectCellsAndStaff(Cell cell, Room room, String items) {
        getObjectCells(cell, room == null ? null : room.getType()).iterator().forEach(c -> c.iterator().forEach(c1 -> {
            if (items != null) {
                Arrays.stream(items.split(";"))
                        .forEach(i -> {
                            if (i.endsWith(c1.getName())) {
                                Items itemType = Items.valueOf(i.substring(0, i.indexOf(ID_DELIMITER)));
                                ObjectCellItem objectCellItem = new ObjectCellItem(i, itemType);
                                c1.addActor(objectCellItem);
                            }
                        });
            }
            cell.addActor(c1);
        }));
    }

    public void addEmptyObjectCells(Cell cell, Room room) {
        getObjectCells(cell, room.getType()).iterator().forEach(c -> c.iterator().forEach(cell::addActor));
    }

    public boolean isBuilt(Cell cell) {
        Room room = getCellRoom(cell);
        if (room == null) {
            return false;
        }
        Array<RoomBuildingModel> tasks = (Array<RoomBuildingModel>) gameCache.getObject(GameCache.BUILD_TASKS);
        for (RoomBuildingModel m : tasks) {
            if (m.getRoomInfo().getId().equals(room.getRoomInfo().getId()) && !m.getRoom().isDone()) {
                return false;
            }
        }
        return true;
    }
    
}
