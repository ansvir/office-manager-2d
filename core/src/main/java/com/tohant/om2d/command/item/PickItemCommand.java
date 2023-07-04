package com.tohant.om2d.command.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.CELL;
import static com.tohant.om2d.storage.Cache.CURRENT_ITEM;

public class PickItemCommand implements Command {

    private final String itemId;

    public PickItemCommand(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        Item item = (Item) uiActorService.getActorById(this.itemId);
        RuntimeCacheService.getInstance().setObject(CURRENT_ITEM, item);
        Array<Actor> cells = uiActorService.getActorsByIdPrefix(CELL.name());
        cells.iterator().forEach(c -> {
            if (c instanceof ToggleActor) {
                new ForceToggleCommand(c.getName(), true).execute();
            }
        });
    }

}
