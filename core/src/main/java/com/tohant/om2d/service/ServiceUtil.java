package com.tohant.om2d.service;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.*;
import com.tohant.om2d.actor.constant.CompanyConstant;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.storage.database.ProgressDao;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.*;

public class ServiceUtil {

    public static final String ID_PATTERN = UiActorService.UiComponentConstant.OBJECT_CELL.name() + COORD_DELIMITER + "%d" + COORD_DELIMITER + "%d" + ID_DELIMITER
            + CELL.name() + COORD_DELIMITER + "%d" + COORD_DELIMITER + "%d" + ID_DELIMITER
            + UiActorService.UiComponentConstant.GRID.name() + COORD_DELIMITER + "%d" + ID_DELIMITER
            + UiActorService.UiComponentConstant.OFFICE.name() + COORD_DELIMITER + "%s" + ID_DELIMITER
            + "COMPANY" + COORD_DELIMITER + "%s";
    private static final RuntimeCacheService CACHE_SERVICE = RuntimeCacheService.getInstance();

    public static int nextToHalls(Cell cell) {
        Array<Cell> neighborCells = getNeighborCells(cell);
        int points = 0;
        for (int i = 0; i < neighborCells.size; i++) {
            if (!neighborCells.get(i).isEmpty() && neighborCells.get(i).isBuilt()) {
                Room room = getCellRoom(neighborCells.get(i));
                if (room != null && room.getType() == Room.Type.HALL) {
                    points++;
                }
            }
        }
        return points;
    }

    private static Array<Cell> getNeighborCells(Cell cell) {
        ProgressEntity progressEntity = ProgressDao.getInstance().queryForId(UUID.fromString(CACHE_SERVICE.getValue(Cache.CURRENT_PROGRESS_ID)));
        String level = progressEntity.getLevelEntity().getActorName();
        UiActorService uiActorService = UiActorService.getInstance();
        Array<Cell> cells = new Array<>();
        cells.add((Cell) uiActorService.getActorById(getCellActorId((int) getCellCoordinates(cell).x - 1, (int) getCellCoordinates(cell).y, level)));
        cells.add((Cell) uiActorService.getActorById(getCellActorId((int) getCellCoordinates(cell).x + 1, (int) getCellCoordinates(cell).y, level)));
        cells.add((Cell) uiActorService.getActorById(getCellActorId((int) getCellCoordinates(cell).x, (int) getCellCoordinates(cell).y - 1, level)));
        cells.add((Cell) uiActorService.getActorById(getCellActorId((int) getCellCoordinates(cell).x, (int) getCellCoordinates(cell).y + 1, level)));
        return new Array<>(Arrays.stream(cells.toArray(Cell.class)).filter(Objects::nonNull).toArray(Cell[]::new));
    }

    public static boolean checkHallNextToRoomThatHasNoOtherHalls(Cell hall) {
        Array<Cell> cells = getNeighborCells(hall);
        int points = 0;
        for (Cell c : cells) {
            Room room = getCellRoom(c);
            if (room != null && room.getType() != Room.Type.HALL && nextToHalls(c) <= 1) {
                points++;
            }
        }
        return points > 0;
    }

    public static Room getCellRoom(Cell cell) {
        Array<Actor> cells = cell.getChildren();
        for (int i = 0 ; i < cells.size; i++) {
            if (cells.get(i) instanceof Room) {
                return (Room) cells.get(i);
            }
        }
        return null;
    }

    public static Vector3 getCellCoordinates(Cell cell) {
        String[] objectsNames = cell.getName().split(ID_DELIMITER);
        String[] coords = objectsNames[0].split(COORD_DELIMITER);
        String[] levelCoords = objectsNames[1].split(COORD_DELIMITER);
        return new Vector3(Long.parseLong(coords[1]), Long.parseLong(coords[2]), Long.parseLong(levelCoords[1]));
    }

    public static Vector3 getCellCoordinatesByName(String name) {
        String[] objectsNames = name.split(ID_DELIMITER);
        String[] coords = objectsNames[0].split(COORD_DELIMITER);
        String[] levelCoords = objectsNames[1].split(COORD_DELIMITER);
        return new Vector3(Long.parseLong(coords[1]), Long.parseLong(coords[2]), Long.parseLong(levelCoords[1]));
    }

    public static Vector3 getObjectCellCoordinates(ObjectCell cell) {
        String[] objectsNames = cell.getName().split(ID_DELIMITER);
        String[] coords = objectsNames[0].split(COORD_DELIMITER);
        String[] cellCoords = objectsNames[1].split(COORD_DELIMITER);
        String[] levelCoords = objectsNames[2].split(COORD_DELIMITER);
        return new Vector3(Long.parseLong(coords[1]), Long.parseLong(coords[2]), Long.parseLong(levelCoords[1]));
    }

    public static Vector2 getObjectCellItemCoordinatesByName(String name) {
        String[] nameAndCoords = name.split(COORD_DELIMITER);
        return new Vector2(Long.parseLong(nameAndCoords[1]), Long.parseLong(nameAndCoords[2]));
    }

    public static String getGridIdByCell(Cell cell) {
        String cellName = cell.getName();
        return cellName.substring(cellName.indexOf(ID_DELIMITER) + 1);
    }

    public static String getObjectCellItemId(String itemType, String objectCellId) {
        return itemType + ID_DELIMITER + objectCellId;
    }

    public static String getObjectCellActorId(int objectCellX, int objectCellY, String cellId) {
        return OBJECT_CELL.name() + COORD_DELIMITER + objectCellX + COORD_DELIMITER + objectCellY + ID_DELIMITER + cellId;
    }

    public static String getCellActorId(int cellX, int cellY, String levelId) {
        return CELL.name() + COORD_DELIMITER + cellX + COORD_DELIMITER + cellY + ID_DELIMITER + levelId;
    }

    public static String getRoomActorId(Room room, String cellId) {
        return "ROOM" + COORD_DELIMITER + room.getType().name() + ID_DELIMITER + cellId;
    }

    public static String getStaffActorId(Staff staff, int index, String roomId) {
        return "STAFF" + COORD_DELIMITER + staff.getType().name() + COORD_DELIMITER + index + ID_DELIMITER + roomId;
    }

    public static String getResidentActorId(String residentId, String officeId) {
        return "RESIDENT" + COORD_DELIMITER + residentId + ID_DELIMITER + officeId;
    }

    public static String getGridActorId(int level, String officeId) {
        return GRID.name() + COORD_DELIMITER + level + ID_DELIMITER + officeId;
    }

    public static String getOfficeActorId(String officeId, String companyId) {
        return OFFICE.name() + COORD_DELIMITER + officeId + ID_DELIMITER + "COMPANY" + COORD_DELIMITER + companyId;
    }

    public static String getCompanyActorId(String companyId) {
        return "COMPANY" + COORD_DELIMITER + companyId;
    }

    public static Vector3 getObjectCellCellCoordinates(ObjectCell cell) {
        String objectCellParentName = cell.getName().substring(cell.getName().lastIndexOf(ID_DELIMITER) + 1);
        String[] parentCoords = objectCellParentName.split(COORD_DELIMITER);
        return new Vector3(Long.parseLong(parentCoords[1]), Long.parseLong(parentCoords[2]), Long.parseLong(parentCoords[3]));
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

    public static long getEmployeesAmountByType(Staff.Type type) {
        switch (type) {
            case SECURITY:
                return CACHE_SERVICE.getLong(Cache.TOTAL_SECURITY_STAFF);
            case WORKER:
                return CACHE_SERVICE.getLong(Cache.TOTAL_WORKERS);
            case CLEANING:
                return CACHE_SERVICE.getLong(Cache.TOTAL_CLEANING_STAFF);
            case ADMINISTRATION:
                return CACHE_SERVICE.getLong(Cache.TOTAL_ADMIN_STAFF);
            case CAFFE:
                return CACHE_SERVICE.getLong(Cache.TOTAL_CAFFE_STAFF);
            default:
                return -1L;
        }
    }

    public static void setEmployeesAmountByType(Staff.Type type, long amount) {
        switch (type) {
            case SECURITY:
                CACHE_SERVICE.setLong(Cache.TOTAL_SECURITY_STAFF, amount);
                break;
            case CLEANING:
                CACHE_SERVICE.setLong(Cache.TOTAL_CLEANING_STAFF, amount);
                break;
            case WORKER:
                CACHE_SERVICE.setLong(Cache.TOTAL_WORKERS, amount);
                break;
            case ADMINISTRATION:
                CACHE_SERVICE.setLong(Cache.TOTAL_ADMIN_STAFF, amount);
                break;
            case CAFFE:
                CACHE_SERVICE.setLong(Cache.TOTAL_CAFFE_STAFF, amount);
                break;
            default:
                break;
        }
    }

    public static long getRoomsAmountByType(Room.Type type) {
        switch (type) {
            case OFFICE:
                return CACHE_SERVICE.getLong(Cache.OFFICES_AMOUNT);
            case HALL:
                return CACHE_SERVICE.getLong(Cache.HALLS_AMOUNT);
            case SECURITY:
                return CACHE_SERVICE.getLong(Cache.SECURITY_AMOUNT);
            case CLEANING:
                return CACHE_SERVICE.getLong(Cache.CLEANING_AMOUNT);
            case CAFFE:
                return CACHE_SERVICE.getLong(Cache.CAFFE_AMOUNT);
            case ELEVATOR:
                return CACHE_SERVICE.getLong(Cache.ELEVATOR_AMOUNT);
            default:
                return -1L;
        }
    }

    public static void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE:
                CACHE_SERVICE.setLong(Cache.OFFICES_AMOUNT, amount);
                break;
            case HALL:
                CACHE_SERVICE.setLong(Cache.HALLS_AMOUNT, amount);
                break;
            case SECURITY:
                CACHE_SERVICE.setLong(Cache.SECURITY_AMOUNT, amount);
                break;
            case CLEANING:
                CACHE_SERVICE.setLong(Cache.CLEANING_AMOUNT, amount);
                break;
            case CAFFE:
                CACHE_SERVICE.setLong(Cache.CAFFE_AMOUNT, amount);
                break;
            case ELEVATOR:
                CACHE_SERVICE.setLong(Cache.ELEVATOR_AMOUNT, amount);
            default:
                break;
        }
    }

    public static Room.Type getCurrentRoomType() {
        String value = CACHE_SERVICE.getValue(Cache.CURRENT_ROOM_TYPE);
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

    public static Array<Array<ObjectCell>> getObjectCells(Cell cell, Room.Type room) {
        Vector3 coords = getCellCoordinates(cell);
        int r = (int) coords.x;
        int c = (int) coords.y;
        ProgressEntity progressEntity = ProgressDao.getInstance()
                .queryForId(UUID.fromString(CACHE_SERVICE.getValue(Cache.CURRENT_PROGRESS_ID)));
        String level = progressEntity.getLevelEntity().getActorName();
        Array<Array<ObjectCell>> cells = new Array<>();
        String cellId = getCellActorId(r, c, level);
        if (room == null) {
            for (int i = 0; i <= OBJECT_CELL_SIZE + 1; i++) {
                cells.insert(i, new Array<>());
                for (int j = 0; j <= OBJECT_CELL_SIZE + 1; j++) {
                    cells.get(i).insert(j, new ObjectCell(getObjectCellActorId(i, j, cellId),
                            i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, false));
                }
            }
        } else {
            switch (room) {
                case HALL: {
                    for (int i = 0; i <= OBJECT_CELL_SIZE + 1; i++) {
                        cells.insert(i, new Array<>());
                        for (int j = 0; j <= OBJECT_CELL_SIZE + 1; j++) {
                            cells.get(i).insert(j, new ObjectCell(getObjectCellActorId(i, j, cellId),
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
                                            new ObjectCell(getObjectCellActorId(i, j, cellId),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : j == 0 && i < OBJECT_CELL_SIZE / 2f && i > OBJECT_CELL_SIZE / 2f ?
                                            new ObjectCell(getObjectCellActorId(i, j, cellId),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : i == OBJECT_CELL_SIZE - 1 && j < OBJECT_CELL_SIZE / 2f && j > OBJECT_CELL_SIZE / 2f ?
                                            new ObjectCell(getObjectCellActorId(i, j, cellId),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : j == OBJECT_CELL_SIZE - 1 && i < OBJECT_CELL_SIZE / 2f && i > OBJECT_CELL_SIZE / 2f ?
                                            new ObjectCell(getObjectCellActorId(i, j, cellId),
                                                    i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                            : new ObjectCell(getObjectCellActorId(i, j, cellId),
                                            i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, false));
                        }
                    }
                    break;
                }
            }
        }
        return cells;
    }

    public static void addObjectCellsAndStaff(Cell cell, Room room, String items) {
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

    public static void addEmptyObjectCells(Cell cell, Room room) {
        getObjectCells(cell, room.getType()).iterator().forEach(c -> c.iterator().forEach(cell::addActor));
    }

}
