package com.tohant.om2d.service;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;

import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.storage.CacheImpl.TOTAL_ADMIN_STAFF;

public class ServiceUtil {
    
    private static final CacheService CACHE_SERVICE = CacheService.getInstance(); 

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
        int cellX = Integer.parseInt(cell.getName().substring(cell.getName().indexOf("#") + 1, cell.getName().lastIndexOf("#")));
        int cellY = Integer.parseInt(cell.getName().substring(cell.getName().lastIndexOf("#") + 1));
        return new Vector2(cellX, cellY);
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
            default:
                break;
        }
    }
    

}
