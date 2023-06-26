package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import java.util.concurrent.atomic.AtomicReference;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.ROOM_INFO_LABEL;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.ROOM_INFO_MODAL;
import static com.tohant.om2d.storage.Cache.CURRENT_ROOM;

public class UpdateRoomInfoCommand implements Command {

    private final float deltaTimestamp;

    public UpdateRoomInfoCommand(float deltaTimestamp) {
        this.deltaTimestamp = deltaTimestamp;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        AsyncRoomBuildService roomBuildService = AsyncRoomBuildService.getInstance();
        Cell currentCell = (Cell) uiActorService.getActorById(RuntimeCacheService.getInstance().getValue(CURRENT_ROOM));
        if (currentCell != null) {
            AtomicReference<TimeLineTask<Room>> roomBuildingTimeline = new AtomicReference<>();
            String currentId = currentCell.getRoomModel().getRoomInfo().getId();
            roomBuildService.getTasks().forEach(t -> {
                if (t.getId().equals(currentId)) {
                    roomBuildingTimeline.set(t);
                }
            });
            DefaultModal roomInfoModal = (DefaultModal) uiActorService.getActorById(ROOM_INFO_MODAL.name());
            String title;
            String text;
            if (roomBuildingTimeline.get() != null && !roomBuildingTimeline.get().isFinished()) {
                long days = roomBuildingTimeline.get().getDate().getDays();
                days = currentCell.getRoomModel().getRoomInfo().getBuildTime().getDays() - days;
                long months = roomBuildingTimeline.get().getDate().getMonth();
                months = months == 1L ? 0 : currentCell.getRoomModel().getRoomInfo().getBuildTime().getMonth() - months;
                long years = roomBuildingTimeline.get().getDate().getYears();
                years = years == 1L ? 0 : currentCell.getRoomModel().getRoomInfo().getBuildTime().getYears() - years;
                title = "Construction " + buildSymbol();
                text = "Building " + currentCell.getRoomModel().getRoomInfo().getType()
                        .name().toLowerCase() + " room\n\nTime left: " + days + " d. " + months + " m. " + years + " y.";
            } else {
                title = currentCell.getRoomModel().getRoomInfo().getType().name().charAt(0) +
                        currentCell.getRoomModel().getRoomInfo().getType().name().substring(1).toLowerCase()
                        + " #" + currentCell.getRoomModel().getRoomInfo().getNumber();
                if (currentCell.getRoomModel().getRoomInfo().getType() == Room.Type.OFFICE) {
                    text = ((currentCell.getRoom() == null) ? ""
                            : "Company: " + ((OfficeRoom) currentCell.getRoom()).getCompanyInfo().getName()) + "\n"
                            + "Price: " + Math.round(currentCell.getRoomModel().getRoomInfo().getPrice()) + "$\n"
                            + "Cost: " + Math.round(currentCell.getRoomModel().getRoomInfo().getCost()) + "$/m\n" + "Workers: "
                            + currentCell.getRoomModel().getRoomInfo().getStaff().size;
                } else if (currentCell.getRoomModel().getRoomInfo().getType() == Room.Type.HALL) {
                    text = "Price: " + Math.round(currentCell.getRoomModel().getRoomInfo().getPrice()) + "$\n"
                            + "Cost: " + Math.round(currentCell.getRoomModel().getRoomInfo().getCost()) + "$/m";
                } else {
                    text = "Price: " + Math.round(currentCell.getRoomModel().getRoomInfo().getPrice()) + "$\n"
                            + "Cost: " + Math.round(currentCell.getRoomModel().getRoomInfo().getCost()) + "$/m\n" + "Employees: "
                            + currentCell.getRoomModel().getRoomInfo().getStaff().size;
                }
            }
            roomInfoModal.getTitleLabel().setText(title);
            roomInfoModal.updateContentText(ROOM_INFO_LABEL.name(), text);
        }
    }

    private String buildSymbol() {
        float deltaTime = 87.5f;
        float deltaTimeStampMillis = deltaTimestamp * 1000f;
        if (deltaTimeStampMillis <= deltaTime) {
            return "|";
        } else if (deltaTimeStampMillis <= deltaTime * 2f) {
            return "/";
        } else if (deltaTimeStampMillis <= deltaTime * 3f) {
            return "-";
        } else if (deltaTimeStampMillis <= deltaTime * 4f) {
            return "\\";
        } else if (deltaTimeStampMillis <= deltaTime * 5f) {
            return "|";
        } else if (deltaTimeStampMillis <= deltaTime * 6f) {
            return "/";
        } else if (deltaTimeStampMillis <= deltaTime * 7f) {
            return "-";
        } else if (deltaTimeStampMillis <= deltaTime * 8f) {
            return "\\";
        } else {
            return "|";
        }
    }

}
