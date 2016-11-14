package com.lakeel.profile.notification.presentation.view;

import org.joda.time.DateTime;

import java.util.Locale;

public final class DateFormatter {

    private static final String FORMAT_TODAY = "HH:mm";

    private static final String FORMAT_THIS_WEEK = "E HH:mm";

    private static final String FORMAT_THIS_YEAR = "MM/dd E";

    private static final String FORMAT_OTHER = "yyyy/MM/dd";

    private final DateComparator mDateComparator;

    private final DateTime mDateTime;

    public DateFormatter(long dataTime) {
        mDateTime = new DateTime(dataTime);
        mDateComparator = new DateComparator(mDateTime, new DateTime());
    }

    public String format() {
        String dateString;
        if (mDateComparator.isSameDay()) {
            // Same day.
            dateString = mDateTime.toString(FORMAT_TODAY, Locale.getDefault());
        } else if (mDateComparator.isSameWeek()) {
            // Same week.
            dateString = mDateTime.toString(FORMAT_THIS_WEEK, Locale.getDefault());
        } else if (mDateComparator.isSameYear()) {
            // Same year.
            dateString = mDateTime.toString(FORMAT_THIS_YEAR, Locale.getDefault());
        } else {
            // Other.
            dateString = mDateTime.toString(FORMAT_OTHER, Locale.getDefault());
        }
        return dateString;
    }
}
