package com.tohant.om2d.model.task;

import java.util.Objects;

public class TimeLineDate implements Comparable<TimeLineDate> {

    private long days;
    private long month;
    private long years;

    public TimeLineDate(long days, long month, long years) {
        this.days = days;
        this.month = month;
        this.years = years;
    }

    public TimeLineDate(TimeLineDate date) {
        this.days = date.getDays();
        this.month = date.getMonth();
        this.years = date.getYears();
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getYears() {
        return years;
    }

    public void setYears(long years) {
        this.years = years;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeLineDate)) return false;
        TimeLineDate that = (TimeLineDate) o;
        return getDays() == that.getDays() && getMonth() == that.getMonth() && getYears() == that.getYears();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDays(), getMonth(), getYears());
    }

    @Override
    public int compareTo(TimeLineDate o) {
        long first = this.days + (this.month == 1L ? 0L : this.month * 30L)
                + (this.years == 1L ? 0L : this.years * 12L * 30L);
        long second = o.days + (o.month == 1L ? 0L : o.month * 30L)
                + (o.years == 1L ? 0L : o.years * 12L * 30L);
        return Long.compare(first, second);
    }

    public long getDateMillis() {
        return 1000L * 60L * 60L * 24L * this.days * this.month * this.years;
    }

}
