package com.tohant.om2d.command.office;

import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.service.CommonService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateOfficeInfoCommand implements Command {

    private final GameCache gameCache;
    private final CommonService commonService;
    private final GameActorFactory gameActorService;

    @Override
    public void execute() {
        StringBuilder builder = new StringBuilder();
        builder.append("Rooms:\n");
        for (Room.Type t : Room.Type.values()) {
            builder.append(" ");
            builder.append(t.name().charAt(0));
            builder.append(t.name().substring(1).toLowerCase());
            builder.append(": ");
            builder.append(commonService.getRoomsAmountByType(t));
            builder.append("\n");
        }
        builder.append("Finances:\n");
        builder.append(" Incomes: ");
        builder.append(Math.round(gameCache.getFloat(GameCache.TOTAL_INCOMES)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append(" Costs: ");
        builder.append(Math.round(gameCache.getFloat(GameCache.TOTAL_COSTS)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append(" Salaries: ");
        builder.append(Math.round(gameCache.getFloat(GameCache.TOTAL_SALARIES)));
        builder.append(" $/m");
        DefaultModal modal = (DefaultModal) GameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.OFFICE_INFO_MODAL.name());
        modal.updateContentText(GameActorFactory.UiComponentConstant.OFFICE_INFO_LABEL.name(), builder.toString());
    }

}
