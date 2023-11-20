package com.tohant.om2d.command.office;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.TIMELINE_LABEL;

@Component
@RequiredArgsConstructor
public class UpdateTimeCommand implements Command {

    private final GameCache gameCache;
    private final GameActorFactory gameActorService;

    @Override
    public void execute() {
        String currentTime = gameCache.getValue(GameCache.CURRENT_TIME);
        Label label = (Label) GameActorSearchService.getActorById(TIMELINE_LABEL.name());
        label.setText(currentTime);
        if (label.getX() + label.getWidth() >= Gdx.graphics.getWidth()) {
            label.setSize(label.getWidth() + 20, label.getHeight());
        }
    }
}
