package com.tohant.om2d.service;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.HallRoom;
import com.tohant.om2d.actor.room.Room;

public class ServiceUtil {

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

}
