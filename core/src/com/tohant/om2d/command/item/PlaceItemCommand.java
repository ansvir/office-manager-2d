package com.tohant.om2d.command.item;

import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.actor.ObjectCellItem;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.service.AssetService;

import java.math.BigDecimal;

import static com.tohant.om2d.storage.Cache.*;

public class PlaceItemCommand extends AbstractCommand {

    @Override
    public void execute() {
        Item item = (Item) getRuntimeCacheService().getObject(CURRENT_ITEM);
        ObjectCell objectCell = (ObjectCell) getRuntimeCacheService().getObject(CURRENT_OBJECT_CELL);
        if (objectCell != null && item != null) {
            if (objectCell.hasChildren()) {
                objectCell.clearChildren();
            }
            ObjectCellItem cellItem = new ObjectCellItem(item.getType());
            objectCell.addActor(cellItem);
            objectCell.setObstacle(true);
            getRuntimeCacheService().setFloat(CURRENT_BUDGET,
                    BigDecimal.valueOf(getRuntimeCacheService().getFloat(CURRENT_BUDGET))
                            .subtract(item.getType().getPrice()).floatValue());
        }
        AssetService.getInstance().setCursor(AssetService.GameCursor.MAIN);
    }

}
