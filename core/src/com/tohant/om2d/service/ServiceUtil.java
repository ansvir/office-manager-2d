package com.tohant.om2d.service;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;

import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.storage.CacheImpl.TOTAL_ADMIN_STAFF;

public class ServiceUtil {
    
    private static final RuntimeCacheService CACHE_SERVICE = RuntimeCacheService.getInstance();

    public static int nextToHalls(Cell cell, Array<Actor> children) {
        Vector2 coords = getCellCoordinates(cell);
        int points = 0;
        for (int i = 0; i < children.size; i++) {
            if (children.get(i) instanceof Cell) {
                Cell currCell = (Cell) children.get(i);
                Vector2 currCoords = getCellCoordinates(currCell);
                if (((coords.x - 1 == currCoords.x || coords.x + 1 == currCoords.x) && coords.y == currCoords.y) ||
                        (coords.y - 1 == currCoords.y || coords.y + 1 == currCoords.y) && coords.x == currCoords.x) {
                    if (!currCell.isEmpty() && currCell.isBuilt() && currCell.getRoomModel().getRoomInfo().getType() == Room.Type.HALL) {
                        points++;
                    }
                }
            }
        }
        return points;
    }

    public static boolean checkHallNextToRoomThatHasNoOtherHalls(Cell hall, Array<Actor> children) {
        Vector2 coords = getCellCoordinates(hall);
        int points = 0;
        for (int i = 0; i < children.size; i++) {
            if (children.get(i) instanceof Cell) {
                Cell currCell = (Cell) children.get(i);
                Vector2 currCoords = getCellCoordinates(currCell);
                if (((coords.x - 1 == currCoords.x || coords.x + 1 == currCoords.x) && coords.y == currCoords.y) ||
                        (coords.y - 1 == currCoords.y || coords.y + 1 == currCoords.y) && coords.x == currCoords.x) {
                    if (!currCell.isEmpty() && currCell.isBuilt()
                            && !(currCell.getRoomModel().getRoomInfo().getType() == Room.Type.HALL)) {
                        if (nextToHalls(currCell, children) <= 1) {
                            points++;
                        }
                    }
                }
            }
        }
        return points > 0;
    }

    private static Vector2 getCellCoordinates(Cell cell) {
        String indices = cell.getName().substring(cell.getName().indexOf("#") + 1);
        String yZIndices = indices.substring(indices.indexOf("#") + 1);
        String indexX = indices.substring(0, indices.indexOf("#"));
        int cellX = Integer.parseInt(indexX);
        String indexY = yZIndices.substring(0, yZIndices.indexOf("#"));
        int cellY = Integer.parseInt(indexY);
        int cellZ = Integer.parseInt(yZIndices.substring(yZIndices.indexOf("#") + 1));
        return new Vector2(cellX, cellY);
    }

    public static Vector3 getObjectCellCoordinates(ObjectCell cell) {
        String objectCellName = cell.getName().substring(0, cell.getName().lastIndexOf("_"));
        String objectCellParentName = cell.getName().substring(cell.getName().lastIndexOf("_") + 1);
        String[] coords = objectCellName.split("#");
        String[] parentCoords = objectCellParentName.split("#");
        return new Vector3(Long.parseLong(coords[1]), Long.parseLong(coords[2]), Long.parseLong(parentCoords[3]));
    }

    public static Vector3 getObjectCellCellCoordinates(ObjectCell cell) {
        String objectCellParentName = cell.getName().substring(cell.getName().lastIndexOf("_") + 1);
        String[] parentCoords = objectCellParentName.split("#");
        return new Vector3(Long.parseLong(parentCoords[1]), Long.parseLong(parentCoords[2]), Long.parseLong(parentCoords[3]));
    }

    public static boolean checkNoCellOnGrid(Array<Actor> children) {
        boolean result = true;
        for (int i = 0; i < children.size; i++) {
            Actor a = children.get(i);
            if (a instanceof Cell) {
                if (((Cell) a).getRoomModel() != null && !((Cell) a).isEmpty()) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static long getEmployeesAmountByType(Staff.Type type) {
        switch (type) {
            case SECURITY: return CACHE_SERVICE.getLong(TOTAL_SECURITY_STAFF);
            case WORKER: return CACHE_SERVICE.getLong(TOTAL_WORKERS);
            case CLEANING: return CACHE_SERVICE.getLong(TOTAL_CLEANING_STAFF);
            case ADMINISTRATION: return CACHE_SERVICE.getLong(TOTAL_ADMIN_STAFF);
            case CAFFE: return CACHE_SERVICE.getLong(TOTAL_CAFFE_STAFF);
            default: return -1L;
        }
    }

    public static void setEmployeesAmountByType(Staff.Type type, long amount) {
        switch (type) {
            case SECURITY:
                CACHE_SERVICE.setLong(TOTAL_SECURITY_STAFF, amount);
                break;
            case CLEANING:
                CACHE_SERVICE.setLong(TOTAL_CLEANING_STAFF, amount);
                break;
            case WORKER:
                CACHE_SERVICE.setLong(TOTAL_WORKERS, amount);
                break;
            case ADMINISTRATION:
                CACHE_SERVICE.setLong(TOTAL_ADMIN_STAFF, amount);
                break;
            case CAFFE:
                CACHE_SERVICE.setLong(TOTAL_CAFFE_STAFF, amount);
                break;
            default:
                break;
        }
    }

    public static long getRoomsAmountByType(Room.Type type) {
        switch (type) {
            case OFFICE:
                return CACHE_SERVICE.getLong(OFFICES_AMOUNT);
            case HALL:
                return CACHE_SERVICE.getLong(HALLS_AMOUNT);
            case SECURITY:
                return CACHE_SERVICE.getLong(SECURITY_AMOUNT);
            case CLEANING:
                return CACHE_SERVICE.getLong(CLEANING_AMOUNT);
            case CAFFE:
                return CACHE_SERVICE.getLong(CAFFE_AMOUNT);
            case ELEVATOR:
                return CACHE_SERVICE.getLong(ELEVATOR_AMOUNT);
            default:
                return -1L;
        }
    }

    public static void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE:
                CACHE_SERVICE.setLong(OFFICES_AMOUNT, amount);
                break;
            case HALL:
                CACHE_SERVICE.setLong(HALLS_AMOUNT, amount);
                break;
            case SECURITY:
                CACHE_SERVICE.setLong(SECURITY_AMOUNT, amount);
                break;
            case CLEANING:
                CACHE_SERVICE.setLong(CLEANING_AMOUNT, amount);
                break;
            case CAFFE:
                CACHE_SERVICE.setLong(CAFFE_AMOUNT, amount);
                break;
            case ELEVATOR:
                CACHE_SERVICE.setLong(ELEVATOR_AMOUNT, amount);
            default:
                break;
        }
    }

    public static Room.Type getCurrentRoomType() {
        String value = CACHE_SERVICE.getValue(CURRENT_ROOM_TYPE);
        if (value == null) {
            return null;
        } else {
            return Room.Type.valueOf(value);
        }
    }

}
