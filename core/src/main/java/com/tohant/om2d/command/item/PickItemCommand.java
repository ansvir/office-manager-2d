package com.tohant.om2d.command.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import lombok.RequiredArgsConstructor;

import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.CELL;

@Component
@RequiredArgsConstructor
public class PickItemCommand implements Command {

    private final GameActorFactory gameActorService;

    @Override
    public void execute() {
        Array<Actor> cells = GameActorSearchService.getActorsByIdPrefix(gameActorService, CELL.name());
        cells.iterator().forEach(c -> {
            if (c instanceof ToggleActor) {
                ((ToggleActor) c).forceToggle(true);
            }
        });
    }

}
