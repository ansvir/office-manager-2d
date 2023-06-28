package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.actor.constant.Constant.COORD_DELIMITER;
import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.CELL;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.ROOM_INFO_MODAL;
import static com.tohant.om2d.storage.Cache.*;
import static com.tohant.om2d.storage.Cache.CURRENT_OFFICE_ID;

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
        String currentCompanyId = cache.getValue(CURRENT_COMPANY_ID);
        String currentOfficeId = cache.getValue(CURRENT_OFFICE_ID);
        String cellId = getCellActorId(r, c, (int) cache.getLong(CURRENT_LEVEL), currentOfficeId, currentCompanyId);
        Cell cell = (Cell) UiActorService.getInstance().getActorById(cellId);
        Room.Type nextType = getCurrentRoomType();
        if (cell.isEmpty() && nextType != null) {
            BuildRoomCommand buildRoomCommand = new BuildRoomCommand(cell);
            buildRoomCommand.execute();
            cache.setValue(CURRENT_CELL, cellId);
            new ForceToggleCommand(ROOM_INFO_MODAL.name(), true).execute();
            cache.setValue(CURRENT_ROOM_TYPE, null);
        } else if (!cell.isEmpty()) {
            cache.setValue(CURRENT_CELL, cellId);
            new ForceToggleCommand(ROOM_INFO_MODAL.name(), true).execute();
        } else if (nextType == null) {
            cache.setValue(CURRENT_CELL, null);
            new ForceToggleCommand(ROOM_INFO_MODAL.name(), false).execute();
        }
    }

}
