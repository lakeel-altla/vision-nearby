package com.lakeel.altla.vision.nearby.presentation.view.date;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

final class DateComparator {

    private final int day1;

    private final int day2;

    private final int week1;

    private final int week2;

    private final int year1;

    private final int year2;

    private final int era1;

    private final int era2;

    DateComparator(@NonNull DateTime dateTime1, @NonNull DateTime dateTime2) {
        day1 = dateTime1.getDayOfWeek();
        day2 = dateTime2.getDayOfWeek();

        week1 = dateTime1.getWeekOfWeekyear();
        week2 = dateTime2.getWeekOfWeekyear();

        year1 = dateTime1.getWeekyear();
        year2 = dateTime2.getWeekyear();

        era1 = dateTime1.getEra();
        era2 = dateTime2.getEra();
    }

    boolean isSameDay() {
        return (day1 == day2) && (week1 == week2) && (year1 == year2) && (era1 == era2);
    }

    boolean isSameWeek() {
        return (week1 == week2) && (year1 == year2) && (era1 == era2);
    }

    boolean isSameYear() {
        return (year1 == year2) && (era1 == era2);
    }
}
