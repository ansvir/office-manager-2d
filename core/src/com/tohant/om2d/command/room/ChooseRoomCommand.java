package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.service.ServiceUtil.getCurrentRoomType;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.CELL;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.ROOM_INFO_MODAL;
import static com.tohant.om2d.storage.Cache.CURRENT_LEVEL;
import static com.tohant.om2d.storage.Cache.CURRENT_ROOM;

public class ChooseRoomCommand implements Command {

    private final int r;
    private final int c;

    public ChooseRoomCommand(int r, int c) {
        this.r = r;
        this.c = c;
    }

    @Override
    public void execute() {
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String roomId = CELL.name() + "#" + r + "#" + c + "#" + cache.getLong(CURRENT_LEVEL);
        Cell cell = (Cell) UiActorService.getInstance().getActorById(roomId);
        Room.Type nextType = getCurrentRoomType();
        if (cell.isEmpty() && nextType != null) {
            BuildRoomCommand buildRoomCommand = new BuildRoomCommand(r, c);
            buildRoomCommand.execute();
            cache.setValue(CURRENT_ROOM, roomId);
            ToggleCommand toggleCommand = new ToggleCommand(ROOM_INFO_MODAL.name());
            toggleCommand.execute();
        } else if (nextType == null) {
            cache.setValue(CURRENT_ROOM, null);
        } else {
            cache.setValue(CURRENT_ROOM, roomId);
            ToggleCommand toggleCommand = new ToggleCommand(ROOM_INFO_MODAL.name());
            toggleCommand.execute();
        }
    }

}
