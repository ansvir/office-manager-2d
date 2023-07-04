package com.tohant.om2d.command.room;

import com.tohant.om2d.command.Command;

public class SelectLevelCommand implements Command {

    private final int index;

    public SelectLevelCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException();
//        UiActorService uiActorService = UiActorService.getInstance();
//        for (Actor a : uiActorService.getUiActors()) {
//            if (a.getName().startsWith(GRID.name())) {
//                a.setVisible(false);
//            }
//        }
    }

}
