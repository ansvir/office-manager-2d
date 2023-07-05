package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.ServiceUtil;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.storage.database.ProgressDao;
import com.tohant.om2d.storage.database.ProgressJsonDatabase;

import java.util.UUID;

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
        ProgressEntity progressEntity = ProgressDao.getInstance().queryForId(UUID.fromString(cache.getValue(Cache.CURRENT_PROGRESS_ID)));
        String cellId = ServiceUtil.getCellActorId(r, c, progressEntity.getLevelEntity().getActorName());
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
