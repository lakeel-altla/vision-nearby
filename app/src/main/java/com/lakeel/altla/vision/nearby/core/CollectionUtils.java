package com.lakeel.altla.vision.nearby.core;

import java.util.List;
import java.util.Map;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }

        return false;
    }

    public static boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }

        return false;
    }
}
