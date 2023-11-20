package com.tohant.om2d.command.item;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.actor.ObjectCellItem;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.CellDao;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static com.tohant.om2d.service.CommonService.addItemToCell;
import static com.tohant.om2d.service.CommonService.getObjectCellItemId;
import static com.tohant.om2d.storage.cache.GameCache.*;

@Component
@RequiredArgsConstructor
public class PlaceItemCommand implements Command {

    private final CellDao cellDao;
    private final GameCache gameCache;

    @Override
    public void execute() {
        Item item = (Item) gameCache.getObject(CURRENT_ITEM);
        ObjectCell objectCell = (ObjectCell) gameCache.getObject(CURRENT_OBJECT_CELL);
        if (objectCell != null && item != null
                && Float.compare(gameCache.getFloat(CURRENT_BUDGET), item.getType().getPrice().floatValue()) > 0) {
            if (objectCell.hasChildren()) {
                objectCell.clearChildren();
            }
            gameCache.setFloat(CURRENT_BUDGET,
                    BigDecimal.valueOf(gameCache.getFloat(CURRENT_BUDGET))
                            .subtract(item.getType().getPrice()).floatValue());
            Cell currentCell = (Cell) objectCell.getParent();
            CellEntity cellEntity = cellDao.queryForId(UUID.fromString(currentCell.getName()));
            String itemId = getObjectCellItemId(item.getName(), objectCell.getName());
            ObjectCellItem cellItem = new ObjectCellItem(itemId, item.getType());
            objectCell.addActor(cellItem);
            objectCell.setObstacle(true);
            cellEntity.setItems(addItemToCell(cellEntity.getItems(), itemId));
            cellDao.update(cellEntity);
        }
        gameCache.setObject(CURRENT_ITEM, null);
    }

}
