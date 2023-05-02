package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncTask;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.storage.CachedEventListener;

import static com.tohant.om2d.storage.Cache.*;

public class TimeLineTask implements AsyncTask<String> {

    private long currentDay;
    private long currentMonth;
    private long currentYear;
    private final long waitTime;
    private long prevTime;
    private long time;
    private boolean isFinished;
    private Cache gameCache;
    private CachedEventListener eventListener;

    public TimeLineTask(long waitTime) {
        this.currentDay = 1L;
        this.currentMonth = 1L;
        this.currentYear = 1L;
        this.prevTime = System.currentTimeMillis();
        this.time = this.prevTime;
        this.waitTime = waitTime;
        this.isFinished = false;
        this.gameCache = Cache.getInstance();
        this.eventListener = CachedEventListener.getInstance();
    }

    public TimeLineTask(long currentDay, long currentMonth, long currentYear, long waitTime) {
        this.currentDay = currentDay;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.prevTime = System.currentTimeMillis();
        this.time = this.prevTime;
        this.waitTime = waitTime;
        this.isFinished = false;
        this.gameCache = Cache.getInstance();
        this.eventListener = CachedEventListener.getInstance();
    }

    @Override
    public String call() {
        boolean stop = false;
        while (!stop || this.isFinished) {
            stop = iterateAndGet();
        }
        this.isFinished = true;
        return this.get();
    }

    private boolean iterateAndGet() {
        if (this.prevTime - this.time >= this.waitTime) {
            this.time = this.prevTime;
            return next();
        }
        this.prevTime = System.currentTimeMillis();
        return false;
    }

    private synchronized boolean next() {
        if (this.currentDay >= 30L) {
            if (this.currentMonth >= 12L) {
                this.currentYear++;
                this.currentMonth = 1L;
            } else {
                this.currentMonth++;
            }
            gameCache.setBoolean(IS_PAYDAY, true);
            eventListener.post();
            this.currentDay = 1L;
        } else {
            gameCache.setBoolean(IS_PAYDAY, false);
            this.currentDay++;
        }
        return false;
    }

    public String get() {
        StringBuilder result = new StringBuilder();
        if (this.currentDay < 10) {
            result.append("0");
        }
        result.append(this.currentDay);
        result.append("/");
        if (this.currentMonth < 10) {
            result.append("0");
        }
        result.append(this.currentMonth);
        result.append("/");
        if (this.currentYear < 1000) {
            for (long i = 100; i > 0; i /= 10L) {
                result.append("0");
            }
        }
        result.append(this.currentYear);
        return result.toString();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void forceFinish() {
        this.isFinished = true;
    }

}
