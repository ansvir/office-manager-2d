package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.RuntimeCacheService;

import static com.tohant.om2d.storage.Cache.CURRENT_ROOM_TYPE;

public class ChooseRoomTypeCommand implements Command {

    private final int index;

    public ChooseRoomTypeCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute() {
        Room.Type next = Room.Type.values()[index];
        RuntimeCacheService.getInstance().setValue(CURRENT_ROOM_TYPE, next.name());
    }

}
