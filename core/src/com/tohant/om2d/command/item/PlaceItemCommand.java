package com.tohant.om2d.command.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ObjectCell;
import com.tohant.om2d.actor.ObjectCellItem;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import java.math.BigDecimal;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.CELL;
import static com.tohant.om2d.storage.Cache.*;

public class PlaceItemCommand implements Command {

    @Override
    public void execute() {
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        Item item = (Item) runtimeCache.getObject(CURRENT_ITEM);
        ObjectCell objectCell = (ObjectCell) runtimeCache.getObject(CURRENT_OBJECT_CELL);
        if (objectCell != null && item != null) {
            if (objectCell.hasChildren()) {
                objectCell.clearChildren();
            }
            ObjectCellItem cellItem = new ObjectCellItem(objectCell.getName() + "_" + item.getType().name(), item.getType());
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
        }
        runtimeCache.setObject(CURRENT_ITEM, null);
        AssetService.getInstance().setCursor(AssetService.GameCursor.MAIN);
    }

}
