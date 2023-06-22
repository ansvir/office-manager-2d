package com.tohant.om2d.command.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.CELL;
import static com.tohant.om2d.storage.Cache.CURRENT_ITEM;

public class PickItemCommand extends AbstractCommand {

    private final String itemId;

    public PickItemCommand(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        Item item = (Item) uiActorService.getActorById(this.itemId);
        getRuntimeCacheService().setObject(CURRENT_ITEM, item);
        Array<Actor> cells = uiActorService.getActorsByIdPrefix(CELL.name());
        cells.forEach(c -> {
            if (c instanceof ToggleActor) {
                new ToggleCommand(c.getName()).execute();
            }
        });
        AssetService.getInstance().setCursor(AssetService.GameCursor.PICK_UP);
    }

}
