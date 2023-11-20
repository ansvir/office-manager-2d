package com.tohant.om2d.command.room;

import com.badlogic.gdx.utils.Array;

import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.CommonService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.task.RoomBuildingModel;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class UpdateRoomInfoCommand implements Command {

    private final GameCache gameCache;
    private final GameActorFactory gameActorService;
    private final CommonService commonService;

    private float deltaTimestamp;

    @PostConstruct
    public void init() {
        this.deltaTimestamp = gameCache.getFloat(GameCache.CURRENT_DELTA_TIME);
    }

    @Override
    public void execute() {
        Array<RoomBuildingModel> tasks = (Array<RoomBuildingModel>) gameCache.getObject(GameCache.BUILD_TASKS);
        Cell currentCell = (Cell) GameActorSearchService.getActorById(gameCache.getValue(GameCache.CURRENT_CELL));
        if (currentCell != null) {
            AtomicReference<RoomBuildingModel> buildingModel = new AtomicReference<>();
            String currentId = currentCell.getName();
            tasks.forEach(t -> {
                if (t.getRoomInfo().getId().endsWith(currentId)) {
                    buildingModel.set(t);
                }
            });
            DefaultModal roomInfoModal = (DefaultModal) GameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.ROOM_INFO_MODAL.name());
            Room room = commonService.getCellRoom(currentCell);
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
                roomInfoModal.updateContentText(GameActorFactory.UiComponentConstant.ROOM_INFO_LABEL.name(), text);
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
