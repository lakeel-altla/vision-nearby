package com.lakeel.profile.notification.core;

public final class StringUtils {

    private StringUtils() {
    }

    public static final String EMPTY = "";

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
