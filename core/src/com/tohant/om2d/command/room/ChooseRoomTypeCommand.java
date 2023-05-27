package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.AbstractCommand;

import static com.tohant.om2d.storage.Cache.CURRENT_ROOM_TYPE;

public class ChooseRoomTypeCommand extends AbstractCommand {

    private final int index;

    public ChooseRoomTypeCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute() {
        Room.Type next = Room.Type.values()[index];
        getRuntimeCacheService().setValue(CURRENT_ROOM_TYPE, next.name());
    }

}
