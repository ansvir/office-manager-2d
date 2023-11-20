package com.tohant.om2d.command.ui;

import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.LevelDao;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ToggleGridCommand implements Command {

    private final GameCache gameCache;
    private final GameActorFactory gameActorService;
    private final LevelDao levelDao;

    @Override
    public void execute() {
         toggleGrid();
    }

    private void toggleGrid() {
        LevelEntity levelEntity = levelDao.queryForId(UUID.fromString(gameCache.getValue(GameCache.CURRENT_LEVEL_ID)));
        ((ToggleActor) GameActorSearchService.getActorById(levelEntity.getId().toString())).toggle();
    }

}
