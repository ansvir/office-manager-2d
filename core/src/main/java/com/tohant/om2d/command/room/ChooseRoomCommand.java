package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.service.CommonService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

import static com.tohant.om2d.storage.cache.GameCache.CURRENT_CELL;

@Component
@RequiredArgsConstructor
public class ChooseRoomCommand implements Command {

    private final GameCache gameCache;
    private final GameActorFactory gameActorFactory;
    private final CommonService commonService;
    private final BuildRoomCommand buildRoomCommand;

    @Override
    public void execute() {
        Cell cell = (Cell) GameActorSearchService.getActorById(gameCache.getValue(CURRENT_CELL));
        Room.Type nextType = commonService.getCurrentRoomType();
        if (cell.isEmpty() && nextType != null) {
            buildRoomCommand.execute();
            gameCache.setValue(CURRENT_CELL, cell.getName());
            ((ToggleActor) GameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.ROOM_INFO_MODAL.name()))
                    .forceToggle(true);
            gameCache.setValue(GameCache.CURRENT_ROOM_TYPE, null);
        } else if (!cell.isEmpty()) {
            gameCache.setValue(CURRENT_CELL, cell.getName());
            ((ToggleActor) GameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.ROOM_INFO_MODAL.name()))
                    .forceToggle(true);
        } else {
            gameCache.setValue(CURRENT_CELL, null);
            ((ToggleActor) GameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.ROOM_INFO_MODAL.name()))
                    .forceToggle(false);
        }
    }

}
