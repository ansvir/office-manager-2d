package com.tohant.om2d.command.office;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.Region;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.ProgressDao;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChooseBuildNewOfficeCommand implements Command {

    private final ProgressDao progressDao;
    private final GameCache gameCache;
    private final GameActorFactory gameActorService;

    @Override
    public void execute() {
        Region region = Region.valueOf(gameCache.getValue(GameCache.CURRENT_CHOSEN_REGION));
        ProgressEntity progressEntity = progressDao.queryForId(UUID.fromString(gameCache.getValue(GameCache.CURRENT_PROGRESS_ID)));
        long officesInRegion = progressEntity.getCompanyEntity()
                .getOfficeEntities().stream().filter(o -> o.getRegion() == region)
                .count();
        if (officesInRegion > 0) {
            throw new GameException(GameException.Code.E600);
        }
        float currentBudget = gameCache.getFloat(GameCache.CURRENT_BUDGET);
        if (currentBudget < 1000f) {
            throw new GameException(GameException.Code.E700);
        }
        gameCache.setValue(GameCache.CURRENT_REGION, region.name());
        Actor actor = GameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.BUILD_NEW_OFFICE_MODAL.name());
        ((DefaultModal) actor).forceToggle(true);
    }

}
