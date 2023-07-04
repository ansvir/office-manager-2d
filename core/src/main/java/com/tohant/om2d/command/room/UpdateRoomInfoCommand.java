package com.tohant.om2d.command.room;

import com.badlogic.gdx.utils.Array;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.ServiceUtil;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.task.RoomBuildingModel;

import java.util.concurrent.atomic.AtomicReference;

public class UpdateRoomInfoCommand implements Command {

    private final float deltaTimestamp;

    public UpdateRoomInfoCommand(float deltaTimestamp) {
        this.deltaTimestamp = deltaTimestamp;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
        Array<RoomBuildingModel> tasks = (Array<RoomBuildingModel>) cacheService.getObject(Cache.BUILD_TASKS);
        Cell currentCell = (Cell) uiActorService.getActorById(RuntimeCacheService.getInstance().getValue(Cache.CURRENT_CELL));
        if (currentCell != null) {
            AtomicReference<RoomBuildingModel> buildingModel = new AtomicReference<>();
            String currentId = currentCell.getName();
            tasks.forEach(t -> {
                if (t.getRoomInfo().getId().endsWith(currentId)) {
                    buildingModel.set(t);
                }
            });
            DefaultModal roomInfoModal = (DefaultModal) uiActorService.getActorById(UiActorService.UiComponentConstant.ROOM_INFO_MODAL.name());
            Room room = ServiceUtil.getCellRoom(currentCell);
            String title;
            String text;
            if (room != null) {
                RoomInfo roomInfo = room.getRoomInfo();
                if (buildingModel.get() != null && !buildingModel.get().getTimeLineTask().isFinished()) {
                    TimeLineTask<Room> roomBuildingTimeline = buildingModel.get().getTimeLineTask();
                    long days = roomBuildingTimeline.getDate().getDays();
                    days = roomInfo.getBuildTime().getDays() - days;
                    long months = roomBuildingTimeline.getDate().getMonth();
                    months = months == 1L ? 0 : roomInfo.getBuildTime().getMonth() - months;
                    long years = roomBuildingTimeline.getDate().getYears();
                    years = years == 1L ? 0 : roomInfo.getBuildTime().getYears() - years;
                    title = "Construction " + buildSymbol();
                    text = "Building " + roomInfo.getType()
                            .name().toLowerCase() + " room\n\nTime left: " + days + " d. " + months + " m. " + years + " y.";
                } else {
                    title = roomInfo.getType().name().charAt(0) +
                            roomInfo.getType().name().substring(1).toLowerCase()
                            + " #" + roomInfo.getNumber();
                    if (roomInfo.getType() == Room.Type.OFFICE) {
                        text = "Company: " + ((OfficeRoom) room).getCompanyInfo().getName() + "\n"
                                + "Price: " + Math.round(roomInfo.getPrice()) + "$\n"
                                + "Cost: " + Math.round(roomInfo.getCost()) + "$/m\n" + "Workers: "
                                + roomInfo.getStaff().size;
                    } else if (roomInfo.getType() == Room.Type.HALL) {
                        text = "Price: " + Math.round(roomInfo.getPrice()) + "$\n"
                                + "Cost: " + Math.round(roomInfo.getCost()) + "$/m";
                    } else {
                        text = "Price: " + Math.round(roomInfo.getPrice()) + "$\n"
                                + "Cost: " + Math.round(roomInfo.getCost()) + "$/m\n" + "Employees: "
                                + roomInfo.getStaff().size;
                    }
                }
                roomInfoModal.getTitleLabel().setText(title);
                roomInfoModal.updateContentText(UiActorService.UiComponentConstant.ROOM_INFO_LABEL.name(), text);
            }
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
