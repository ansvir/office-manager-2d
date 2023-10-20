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

    public TimeLineDate(String date) {
        String[] parts = date.split("/");
        this.days = Long.parseLong(parts[0]);
        this.month = Long.parseLong(parts[1]);
        this.years = Long.parseLong(parts[2]);
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

    public long toMillis() {

        long millis = 0;
        millis += (this.years - 1) * 365L * 24L * 60L * 60L * 1000L;
        millis += (this.month - 1) * 30L * 24L * 60L * 60L * 1000L;
        millis += (this.days - 1) * 24L * 60L * 60L * 1000L;

        return millis;
    }


    public static TimeLineDate fromMillis(long millis) {
        long years = millis / (365L * 24L * 60L * 60L * 1000L);
        millis -= years * (365L * 24L * 60L * 60L * 1000L);
        long months = millis / (30L * 24L * 60L * 60L * 1000L);
        millis -= months * (30L * 24L * 60L * 60L * 1000L);
        long days = millis / (24L * 60L * 60L * 1000L);
        days = Math.max(1, Math.min(days, 30));
        months = Math.max(1, Math.min(months, 12));
        years = Math.max(1, years);
        return new TimeLineDate(days, months, years);
    }

    public static TimeLineDate fromDateString(String date) {
        String[] parts = date.split("/");
        long days = Long.parseLong(parts[0]);
        long months = Long.parseLong(parts[1]);
        long years = Long.parseLong(parts[2]);
        return new TimeLineDate(days, months, years);
    }

    public enum Frequency {

        NEVER("never"),
        ONCE("once"),
        ONE_MINUTE("1minute"),
        ONE_DAY("1day"),
        ONE_MONTH("1month"),
        ONE_YEAR("1year");

        private final String value;

        Frequency(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
