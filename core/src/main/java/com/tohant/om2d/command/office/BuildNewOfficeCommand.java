package com.tohant.om2d.command.office;

import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.Region;
import com.tohant.om2d.model.entity.*;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.database.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;
import static com.tohant.om2d.storage.cache.Cache.*;

public class BuildNewOfficeCommand implements Command {

    private final boolean newGame;
    private final Region region;

    public BuildNewOfficeCommand(boolean newGame, Region region) {
        this.newGame = newGame;
        this.region = region;
    }

    @Override
    public void execute() {
        int level = 0;
        List<CellEntity> cells = IntStream.range(0, GRID_HEIGHT).boxed()
                .flatMap(r -> IntStream.range(0, GRID_WIDTH).boxed()
                        .map(c -> new CellEntity(r, c, null)))
                .collect(Collectors.toList());
        LevelEntity levelEntity = new LevelEntity(level, cells);
        OfficeEntity officeEntity = new OfficeEntity(RuntimeCacheService.getInstance().getValue(COMPANY_NAME), 0.0f, List.of(levelEntity), List.of(),
                region);
        ProgressEntity progressEntity = null;
        if (newGame) {
            CompanyEntity companyEntity = new CompanyEntity(RuntimeCacheService.getInstance().getValue(COMPANY_NAME), List.of(officeEntity), 2000.0f);
            CompanyDao.getInstance().create(companyEntity);
            officeEntity.setCompanyEntity(companyEntity);
            OfficeDao.getInstance().create(officeEntity);
            levelEntity.setOfficeEntity(officeEntity);
            LevelDao.getInstance().create(levelEntity);
            progressEntity = new ProgressEntity(companyEntity, officeEntity.getId().toString(),
                    levelEntity.getId().toString(), "01/01/0001");
            ProgressDao.getInstance().create(progressEntity);
            RuntimeCacheService.getInstance().setValue(CURRENT_PROGRESS_ID, progressEntity.getId().toString());
        } else {
            OfficeDao.getInstance().create(officeEntity);
            levelEntity.setOfficeEntity(officeEntity);
            LevelDao.getInstance().create(levelEntity);
            progressEntity = ProgressDao.getInstance()
                    .queryForId(UUID.fromString(RuntimeCacheService.getInstance().getValue(CURRENT_PROGRESS_ID)));
            progressEntity.getCompanyEntity().getOfficeEntities().add(officeEntity);
            ProgressDao.getInstance().update(progressEntity);
            RuntimeCacheService.getInstance().setValue(CURRENT_TIME, progressEntity.getTimeline());
        }
        CellDao cellDao = CellDao.getInstance();
        cells.forEach(c -> {
            c.setLevelEntity(levelEntity);
            cellDao.create(c);
        });
        RuntimeCacheService.getInstance().setValue(CURRENT_TIME, progressEntity.getTimeline());
        RuntimeCacheService.getInstance().setFloat(CURRENT_BUDGET, progressEntity.getCompanyEntity().getBudget());
        RuntimeCacheService.getInstance().setValue(CURRENT_OFFICE_ID, officeEntity.getId().toString());
        RuntimeCacheService.getInstance().setValue(CURRENT_LEVEL_ID, levelEntity.getId().toString());
        RuntimeCacheService.getInstance().setBoolean(READY_TO_START, true);
    }

}
