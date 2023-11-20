package com.tohant.om2d.command.item;

import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

import static com.tohant.om2d.storage.cache.GameCache.CURRENT_OBJECT_CELL;

@Component
@RequiredArgsConstructor
public class DestroyItemCommand implements Command {

    private final GameCache gameCache;

    @Override
    public void execute() {
        ObjectCell objectCell = (ObjectCell) gameCache.getObject(CURRENT_OBJECT_CELL);
        objectCell.clearChildren();
        objectCell.setObstacle(false);
    }

}
