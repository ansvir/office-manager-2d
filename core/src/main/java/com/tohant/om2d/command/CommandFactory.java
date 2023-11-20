package com.tohant.om2d.command;

import com.tohant.om2d.command.item.PickItemCommand;
import com.tohant.om2d.command.office.BuildNewOfficeCommand;
import com.tohant.om2d.command.room.ChooseRoomCommand;
import com.tohant.om2d.command.room.DestroyRoomCommand;
import com.tohant.om2d.command.ui.CreateWorldModalCommand;
import com.tohant.om2d.command.ui.ToggleGridCommand;
import com.tohant.om2d.di.annotation.Component;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class CommandFactory {

    private final BuildNewOfficeCommand buildNewOfficeCommand;
    private final DestroyRoomCommand destroyRoomCommand;
    private final ChooseRoomCommand chooseRoomCommand;
    private final CreateWorldModalCommand createWorldModalCommand;
    private final PickItemCommand pickItemCommand;
    private final ToggleGridCommand toggleGridCommand;

}
