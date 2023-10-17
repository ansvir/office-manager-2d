package com.tohant.om2d.command.item;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.actor.ObjectCellItem;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.database.CellDao;

import java.math.BigDecimal;
import java.util.UUID;

import static com.tohant.om2d.service.ServiceUtil.addItemToCell;
import static com.tohant.om2d.service.ServiceUtil.getObjectCellItemId;
import static com.tohant.om2d.storage.cache.Cache.*;

public class PlaceItemCommand implements Command {

    @Override
    public void execute() {
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        Item item = (Item) runtimeCache.getObject(CURRENT_ITEM);
        ObjectCell objectCell = (ObjectCell) runtimeCache.getObject(CURRENT_OBJECT_CELL);
        if (objectCell != null && item != null
                && Float.compare(runtimeCache.getFloat(CURRENT_BUDGET), item.getType().getPrice().floatValue()) > 0) {
            if (objectCell.hasChildren()) {
                objectCell.clearChildren();
            }
            runtimeCache.setFloat(CURRENT_BUDGET,
                    BigDecimal.valueOf(runtimeCache.getFloat(CURRENT_BUDGET))
                            .subtract(item.getType().getPrice()).floatValue());
            Cell currentCell = (Cell) objectCell.getParent();
            CellEntity cellEntity = CellDao.getInstance().queryForId(UUID.fromString(currentCell.getName()));
            String itemId = getObjectCellItemId(item.getName(), objectCell.getName());
            ObjectCellItem cellItem = new ObjectCellItem(itemId, item.getType());
            objectCell.addActor(cellItem);
            objectCell.setObstacle(true);
            cellEntity.setItems(addItemToCell(cellEntity.getItems(), itemId));
            CellDao.getInstance().update(cellEntity);
        }
        runtimeCache.setObject(CURRENT_ITEM, null);
    }

}
