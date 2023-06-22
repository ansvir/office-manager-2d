package com.tohant.om2d.command.item;

import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.command.AbstractCommand;

import static com.tohant.om2d.storage.Cache.CURRENT_OBJECT_CELL;

public class DestroyItemCommand extends AbstractCommand {

    @Override
    public void execute() {
        ObjectCell objectCell = (ObjectCell) getRuntimeCacheService().getObject(CURRENT_OBJECT_CELL);
        objectCell.clearChildren();
        objectCell.setObstacle(false);
    }

}
