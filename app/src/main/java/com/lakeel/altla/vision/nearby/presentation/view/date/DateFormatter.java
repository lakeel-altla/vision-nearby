package com.lakeel.altla.vision.nearby.presentation.view.date;

import org.joda.time.DateTime;

import java.util.Locale;

public final class DateFormatter {

    private static final String FORMAT_TODAY = "HH:mm";

    private static final String FORMAT_THIS_WEEK = "E HH:mm";

    private static final String FORMAT_THIS_YEAR = "MM/dd E";

    private static final String FORMAT_OTHER = "yyyy/MM/dd";

    private final DateComparator dateComparator;

    private final DateTime dateTime;

    public DateFormatter(long time) {
        dateTime = new DateTime(time);
        dateComparator = new DateComparator(dateTime, new DateTime());
    }

    public String format() {
        String dateString;
        if (dateComparator.isSameDay()) {
            // Same day.
            dateString = dateTime.toString(FORMAT_TODAY, Locale.getDefault());
        } else if (dateComparator.isSameWeek()) {
            // Same week.
            dateString = dateTime.toString(FORMAT_THIS_WEEK, Locale.getDefault());
        } else if (dateComparator.isSameYear()) {
            // Same year.
            dateString = dateTime.toString(FORMAT_THIS_YEAR, Locale.getDefault());
        } else {
            // Other.
            dateString = dateTime.toString(FORMAT_OTHER, Locale.getDefault());
        }
        return dateString;
    }
}
