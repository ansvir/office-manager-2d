package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncTask;

public class TimeLineTask implements AsyncTask<String> {

    private long currentDay;
    private long currentMonth;
    private long currentYear;
    private final long waitTime;
    private long prevTime;
    private long time;
    private boolean isFinished;

    public TimeLineTask(long waitTime) {
        this.currentDay = 0L;
        this.currentMonth = 1L;
        this.currentYear = 1L;
        this.prevTime = System.currentTimeMillis();
        this.time = this.prevTime;
        this.waitTime = waitTime;
        this.isFinished = false;
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
                this.currentMonth = 0L;
            } else {
                this.currentMonth++;
            }
            this.currentDay = 0L;
        } else {
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
