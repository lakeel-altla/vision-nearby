package com.lakeel.profile.notification.presentation.view;

import org.joda.time.DateTime;

public final class DateComparator {

    private final int mDay1;

    private final int mDay2;

    private final int mWeek1;

    private final int mWeek2;

    private final int mYear1;

    private final int mYear2;

    private final int mEra1;

    private final int mEra2;

    public DateComparator(DateTime dateTime1, DateTime dateTime2) {
        mDay1 = dateTime1.getDayOfWeek();
        mDay2 = dateTime2.getDayOfWeek();

        mWeek1 = dateTime1.getWeekOfWeekyear();
        mWeek2 = dateTime2.getWeekOfWeekyear();

        mYear1 = dateTime1.getWeekyear();
        mYear2 = dateTime2.getWeekyear();

        mEra1 = dateTime1.getEra();
        mEra2 = dateTime2.getEra();
    }

    public boolean isSameDay() {
        return (mDay1 == mDay2) && (mWeek1 == mWeek2) && (mYear1 == mYear2) && (mEra1 == mEra2);
    }

    public boolean isSameWeek() {
        return (mWeek1 == mWeek2) && (mYear1 == mYear2) && (mEra1 == mEra2);
    }

    public boolean isSameYear() {
        return (mYear1 == mYear2) && (mEra1 == mEra2);
    }
}
