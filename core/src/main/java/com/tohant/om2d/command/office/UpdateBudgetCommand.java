package com.tohant.om2d.command.office;

import com.badlogic.gdx.graphics.Color;
import com.tohant.om2d.actor.ui.label.GameStandaloneLabel;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateBudgetCommand implements Command {

    private final GameCache gameCache;
    private final GameActorFactory gameActorService;

    @Override
    public void execute() {
        float budget = gameCache.getFloat(GameCache.CURRENT_BUDGET);
        GameStandaloneLabel label = (GameStandaloneLabel) GameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.BUDGET_LABEL.name());
        if (budget < 0) {
            label.setColor(Color.RED);
        } else {
            label.setColor(Color.GREEN);
        }
        label.setText(Math.round(budget) + " $");
    }

}
