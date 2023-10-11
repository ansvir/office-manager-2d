package com.tohant.om2d.command.global;

import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.json.EventJson;

public class EventCommand implements Command {

    private final EventJson eventJson;

    public EventCommand(EventJson eventJson) {
        this.eventJson = eventJson;
    }

    @Override
    public void execute() {

    }

}
