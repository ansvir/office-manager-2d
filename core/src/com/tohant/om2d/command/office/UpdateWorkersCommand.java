package com.tohant.om2d.command.office;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.environment.Man;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.service.RuntimeCacheService;

import static com.tohant.om2d.storage.Cache.TOTAL_WORKERS;
import static com.tohant.om2d.storage.Cache.WORKERS;

public class UpdateWorkersCommand extends AbstractCommand {

    @Override
    public void execute() {
        RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
        long totalWorkers = cacheService.getLong(TOTAL_WORKERS);
        Array<Man> workers = (Array<Man>) cacheService.getObject(WORKERS);
        int leftWorkers = totalWorkers - workers.size < 0 ? 0 : (int) (totalWorkers - workers.size);
        Array<Man> currentWorkers = new Array<>(leftWorkers);
        int index = 0;
        for (int i = index; i < leftWorkers; i++, index++) {
            currentWorkers.add(workers.get(i));
        }
        for (int i = 0; i < index; i++) {
            currentWorkers.add(new Man());
        }
        cacheService.setObject(WORKERS, currentWorkers);
    }

}
