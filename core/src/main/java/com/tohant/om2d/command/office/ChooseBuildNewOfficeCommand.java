package com.tohant.om2d.command.office;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.Region;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.storage.database.ProgressDao;

import java.util.UUID;

public class ChooseBuildNewOfficeCommand implements Command {

    private final Region region;

    public ChooseBuildNewOfficeCommand(Region region) {
        this.region = region;
    }

    @Override
    public void execute() {
        ProgressEntity progressEntity = ProgressDao.getInstance().queryForId(UUID.fromString(RuntimeCacheService.getInstance().getValue(Cache.CURRENT_PROGRESS_ID)));
        long officesInRegion = progressEntity.getCompanyEntity()
                .getOfficeEntities().stream().filter(o -> o.getRegion() == region)
                .count();
        if (officesInRegion > 0) {
            throw new GameException(GameException.Code.E600);
        }
        float currentBudget = RuntimeCacheService.getInstance().getFloat(Cache.CURRENT_BUDGET);
        if (currentBudget < 1000f) {
            throw new GameException(GameException.Code.E700);
        }
        RuntimeCacheService.getInstance().setValue(Cache.CURRENT_REGION, region.name());
        Actor actor = UiActorService.getInstance().getActorById(UiActorService.UiComponentConstant.BUILD_NEW_OFFICE_MODAL.name());
        ((DefaultModal) actor).forceToggle(true);
    }

}
