package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncTask;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class TimeLineTask<T> extends CompletableFuture<T> implements AsyncTask<T> {

    private final String id;
    private final TimeLineDate date;
    private final long waitTime;
    private long prevTime;
    private long time;
    private boolean isFinished;
    private final T result;
    private final Runnable updateCallback;
    private final Predicate<TimeLineDate> stopCondition;
    private final Runnable successCallback;

    public TimeLineTask(long waitTime, Runnable updateCallback, T result) {
        this.id = UUID.randomUUID().toString();
        this.date = new TimeLineDate(1L, 1L ,1L);
        this.prevTime = System.currentTimeMillis();
        this.time = this.prevTime;
        this.waitTime = waitTime;
        this.result = result;
        this.stopCondition = (z) -> false;
        this.updateCallback = updateCallback;
        this.successCallback = () -> {};
    }

    public TimeLineTask(String id, long waitTime, T result, Predicate<TimeLineDate> stopCondition, Runnable updateCallback, Runnable successCallback) {
        this.id = id;
        this.date = new TimeLineDate(1L, 1L ,1L);
        this.prevTime = System.currentTimeMillis();
        this.time = this.prevTime;
        this.waitTime = waitTime;
        this.result = result;
        this.stopCondition = stopCondition;
        this.updateCallback = updateCallback;
        this.successCallback = successCallback;
    }

    @Override
    public T get() {
        boolean stop = false;
        while (!stop && !this.isFinished) {
            if (this.stopCondition != null) {
                if (this.stopCondition.test(this.date)) {
                    successCallback.run();
                    forceFinish();
                    break;
                }
            }
            stop = iterateAndGet();
        }
        return this.result;
    }

    private synchronized boolean iterateAndGet() {
        if (isTimeUpdated()) {
            this.time = this.prevTime;
            updateCallback.run();
            return next();
        }
        this.prevTime = System.currentTimeMillis();
        return false;
    }

    private synchronized boolean next() {
        long currentDay = this.date.getDays();
        long currentMonth = this.date.getMonth();
        long currentYear = this.date.getYears();
        if (currentDay >= 30L) {
            if (currentMonth >= 12L) {
                currentYear++;
                currentMonth = 1L;
            } else {
                currentMonth++;
            }
            currentDay = 1L;
        } else {
            currentDay++;
        }
        this.date.setDays(currentDay);
        this.date.setMonth(currentMonth);
        this.date.setYears(currentYear);
        return false;
    }

    public String getDateString() {
        long currentDay = this.date.getDays();
        long currentMonth = this.date.getMonth();
        long currentYear = this.date.getYears();
        StringBuilder result = new StringBuilder();
        if (currentDay < 10) {
            result.append("0");
        }
        result.append(currentDay);
        result.append("/");
        if (currentMonth < 10) {
            result.append("0");
        }
        result.append(currentMonth);
        result.append("/");
        if (currentYear < 1000) {
            for (long i = 100; i > 0; i /= 10L) {
                result.append("0");
            }
        }
        result.append(currentYear);
        return result.toString();
    }

    public boolean isFinished() {
        return isCancelled();
    }

    public void forceFinish() {
        cancel(true);
    }

    public TimeLineDate getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public long getPrevTime() {
        return this.prevTime;
    }

    public long getTime() {
        return this.time;
    }

    public long getWaitTime() {
        return waitTime;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.isFinished = true;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return this.isFinished;
    }

    @Override
    public boolean isDone() {
        return this.isFinished;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return get();
    }

    @Override
    public T call() throws Exception {
        return get();
    }

    public boolean isTimeUpdated() {
        return this.prevTime - this.time >= this.waitTime;
    }
}
