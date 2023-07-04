package com.tohant.om2d.command.item;

import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.service.RuntimeCacheService;

import static com.tohant.om2d.storage.Cache.CURRENT_OBJECT_CELL;

public class DestroyItemCommand implements Command {

    @Override
    public void execute() {
        ObjectCell objectCell = (ObjectCell) RuntimeCacheService.getInstance().getObject(CURRENT_OBJECT_CELL);
        objectCell.clearChildren();
        objectCell.setObstacle(false);
    }

}
