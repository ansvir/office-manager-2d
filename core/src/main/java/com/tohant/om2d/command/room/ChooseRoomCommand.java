package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.ServiceUtil;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.cache.Cache;

public class ChooseRoomCommand implements Command {

    private final String cellId;

    public ChooseRoomCommand(String cellId) {
        this.cellId = cellId;
    }

    @Override
    public void execute() {
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        Cell cell = (Cell) UiActorService.getInstance().getActorById(cellId);
        Room.Type nextType = ServiceUtil.getCurrentRoomType();
        if (cell.isEmpty() && nextType != null) {
            BuildRoomCommand buildRoomCommand = new BuildRoomCommand(cell);
            buildRoomCommand.execute();
            cache.setValue(Cache.CURRENT_CELL, cellId);
            new ForceToggleCommand(UiActorService.UiComponentConstant.ROOM_INFO_MODAL.name(), true).execute();
            cache.setValue(Cache.CURRENT_ROOM_TYPE, null);
        } else if (!cell.isEmpty()) {
            cache.setValue(Cache.CURRENT_CELL, cellId);
            new ForceToggleCommand(UiActorService.UiComponentConstant.ROOM_INFO_MODAL.name(), true).execute();
        } else if (nextType == null) {
            cache.setValue(Cache.CURRENT_CELL, null);
            new ForceToggleCommand(UiActorService.UiComponentConstant.ROOM_INFO_MODAL.name(), false).execute();
        }
    }

}
