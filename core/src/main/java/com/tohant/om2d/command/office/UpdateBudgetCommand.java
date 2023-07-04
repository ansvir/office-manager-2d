package com.tohant.om2d.command.office;

import com.badlogic.gdx.graphics.Color;
import com.tohant.om2d.actor.ui.label.GameStandaloneLabel;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.command.Command;

public class UpdateBudgetCommand implements Command {

    private final float budget;

    public UpdateBudgetCommand(float budget) {
        this.budget = budget;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        GameStandaloneLabel label = (GameStandaloneLabel) uiActorService.getActorById(UiActorService.UiComponentConstant.BUDGET_LABEL.name());
        if (budget < 0) {
            label.setColor(Color.RED);
        } else {
            label.setColor(Color.GREEN);
        }
        label.setText(Math.round(budget) + " $");
    }

}
