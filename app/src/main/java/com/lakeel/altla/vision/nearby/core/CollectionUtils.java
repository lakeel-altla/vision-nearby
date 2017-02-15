package com.lakeel.altla.vision.nearby.core;

import java.util.List;
import java.util.Map;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }
}
