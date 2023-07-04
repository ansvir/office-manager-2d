package com.tohant.om2d.command.office;

import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.command.Command;

import static com.tohant.om2d.service.ServiceUtil.getRoomsAmountByType;

public class UpdateOfficeInfoCommand implements Command {

    @Override
    public void execute() {
        RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
        StringBuilder builder = new StringBuilder();
        builder.append("Rooms:\n");
        for (Room.Type t : Room.Type.values()) {
            builder.append(" ");
            builder.append(t.name().charAt(0));
            builder.append(t.name().substring(1).toLowerCase());
            builder.append(": ");
            builder.append(getRoomsAmountByType(t));
            builder.append("\n");
        }
        builder.append("Finances:\n");
        builder.append(" Incomes: ");
        builder.append(Math.round(cacheService.getFloat(Cache.TOTAL_INCOMES)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append(" Costs: ");
        builder.append(Math.round(cacheService.getFloat(Cache.TOTAL_COSTS)));
        builder.append(" $/m");
        builder.append("\n");
        builder.append(" Salaries: ");
        builder.append(Math.round(cacheService.getFloat(Cache.TOTAL_SALARIES)));
        builder.append(" $/m");
        DefaultModal modal = (DefaultModal) UiActorService.getInstance().getActorById(UiActorService.UiComponentConstant.OFFICE_INFO_MODAL.name());
        modal.updateContentText(UiActorService.UiComponentConstant.OFFICE_INFO_LABEL.name(), builder.toString());
    }

}
