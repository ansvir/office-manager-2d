package com.tohant.om2d.command.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.*;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.UiActorService;

import java.math.BigDecimal;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.CELL;
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
            ObjectCellItem cellItem = new ObjectCellItem(objectCell.getName() + "_" + item.getType().name(), item.getType());
            objectCell.addActor(cellItem);
            objectCell.setObstacle(true);
            getRuntimeCacheService().setFloat(CURRENT_BUDGET,
                    BigDecimal.valueOf(getRuntimeCacheService().getFloat(CURRENT_BUDGET))
                            .subtract(item.getType().getPrice()).floatValue());
            Array<Actor> cells = UiActorService.getInstance().getActorsByIdPrefix(CELL.name());
            cells.iterator().forEach(c -> {
                if (c instanceof ToggleActor) {
                    new ForceToggleCommand(c.getName(), false).execute();
                }
            });
        }
        getRuntimeCacheService().setObject(CURRENT_ITEM, null);
        AssetService.getInstance().setCursor(AssetService.GameCursor.MAIN);
    }

}
