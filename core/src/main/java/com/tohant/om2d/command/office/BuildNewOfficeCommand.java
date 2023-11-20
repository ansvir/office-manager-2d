package com.tohant.om2d.command.office;

import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.model.Region;
import com.tohant.om2d.model.entity.*;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;
import static com.tohant.om2d.storage.cache.GameCache.*;

@Component
@RequiredArgsConstructor
public class BuildNewOfficeCommand implements Command {

    private final GameCache cache;
    private final CellDao cellDao;
    private final CompanyDao companyDao;
    private final OfficeDao officeDao;
    private final LevelDao levelDao;
    private final ProgressDao progressDao;

    @Override
    public void execute() {
        int level = 0;
        Region region = Region.valueOf(cache.getValue(CURRENT_REGION));
        List<CellEntity> cells = IntStream.range(0, GRID_HEIGHT).boxed()
                .flatMap(r -> IntStream.range(0, GRID_WIDTH).boxed()
                        .map(c -> new CellEntity(r, c, null)))
                .collect(Collectors.toList());
        LevelEntity levelEntity = new LevelEntity(level, cells);
        OfficeEntity officeEntity = new OfficeEntity(cache.getValue(COMPANY_NAME), 0.0f, List.of(levelEntity), List.of(),
                region);
        ProgressEntity progressEntity;
        if (cache.getBoolean(NEW_GAME)) {
            CompanyEntity companyEntity = new CompanyEntity(cache.getValue(COMPANY_NAME), List.of(officeEntity), 2000.0f);
            companyDao.create(companyEntity);
            officeEntity.setCompanyEntity(companyEntity);
            officeDao.create(officeEntity);
            levelEntity.setOfficeEntity(officeEntity);
            levelDao.create(levelEntity);
            progressEntity = new ProgressEntity(companyEntity, officeEntity.getId().toString(),
                    levelEntity.getId().toString(), "01/01/0001");
            progressDao.create(progressEntity);
            cache.setValue(CURRENT_PROGRESS_ID, progressEntity.getId().toString());
        } else {
            progressEntity = progressDao.queryForId(UUID.fromString(cache.getValue(CURRENT_PROGRESS_ID)));
            CompanyEntity companyEntity = progressEntity.getCompanyEntity();
            companyEntity.getOfficeEntities().add(officeEntity);
            officeEntity.setCompanyEntity(companyEntity);
            officeDao.create(officeEntity);
            levelEntity.setOfficeEntity(officeEntity);
            levelDao.create(levelEntity);
            progressDao.update(progressEntity);
            cache.setValue(CURRENT_TIME, progressEntity.getTimeline());
        };
        cells.forEach(c -> {
            c.setLevelEntity(levelEntity);
            cellDao.create(c);
        });
        cache.setValue(CURRENT_TIME, progressEntity.getTimeline());
        cache.setFloat(CURRENT_BUDGET, progressEntity.getCompanyEntity().getBudget());
        cache.setValue(CURRENT_OFFICE_ID, officeEntity.getId().toString());
        cache.setValue(CURRENT_LEVEL_ID, levelEntity.getId().toString());
        cache.setBoolean(NEW_GAME, false);
        cache.setBoolean(READY_TO_START, true);
    }

}
