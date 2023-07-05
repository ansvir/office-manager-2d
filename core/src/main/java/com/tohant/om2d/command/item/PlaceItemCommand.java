package com.tohant.om2d.command.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ObjectCellItem;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.ServiceUtil;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.database.CellDao;

import java.math.BigDecimal;

import static com.tohant.om2d.actor.constant.Constant.ID_DELIMITER;
import static com.tohant.om2d.service.ServiceUtil.addItemToCell;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.CELL;
import static com.tohant.om2d.storage.Cache.*;

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
            ObjectCellItem cellItem = new ObjectCellItem(item.getType().name() + ID_DELIMITER + objectCell.getName(), item.getType());
            objectCell.addActor(cellItem);
            objectCell.setObstacle(true);
            runtimeCache.setFloat(CURRENT_BUDGET,
                    BigDecimal.valueOf(runtimeCache.getFloat(CURRENT_BUDGET))
                            .subtract(item.getType().getPrice()).floatValue());
            Array<Actor> cells = UiActorService.getInstance().getActorsByIdPrefix(CELL.name());
            cells.iterator().forEach(c -> {
                if (c instanceof ToggleActor) {
                    new ForceToggleCommand(c.getName(), false).execute();
                }
            });
            CellEntity cellEntity = CellDao.getInstance()
                    .queryForActorName(objectCell.getName().substring(objectCell.getName().indexOf(ID_DELIMITER) + 1));
            cellEntity.setItems(addItemToCell(cellEntity.getItems(), item.getType().name()));
            CellDao.getInstance().update(cellEntity);
        }
        runtimeCache.setObject(CURRENT_ITEM, null);
    }

}
