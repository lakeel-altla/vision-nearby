package com.lakeel.altla.vision.nearby.presentation.view;

import org.joda.time.DateTime;

public final class DateComparator {

    private final int day1;

    private final int day2;

    private final int week1;

    private final int week2;

    private final int year1;

    private final int year2;

    private final int era1;

    private final int era2;

    public DateComparator(DateTime dateTime1, DateTime dateTime2) {
        day1 = dateTime1.getDayOfWeek();
        day2 = dateTime2.getDayOfWeek();

        week1 = dateTime1.getWeekOfWeekyear();
        week2 = dateTime2.getWeekOfWeekyear();

        year1 = dateTime1.getWeekyear();
        year2 = dateTime2.getWeekyear();

        era1 = dateTime1.getEra();
        era2 = dateTime2.getEra();
    }

    public boolean isSameDay() {
        return (day1 == day2) && (week1 == week2) && (year1 == year2) && (era1 == era2);
    }

    public boolean isSameWeek() {
        return (week1 == week2) && (year1 == year2) && (era1 == era2);
    }

    public boolean isSameYear() {
        return (year1 == year2) && (era1 == era2);
    }
}
