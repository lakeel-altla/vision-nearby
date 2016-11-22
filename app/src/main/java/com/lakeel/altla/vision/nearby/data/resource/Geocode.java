package com.lakeel.altla.vision.nearby.data.resource;

import java.util.List;

public final class Geocode {

    public List<Result> results;

    public static class Result {

        public List<AddressComponents> address_components;

        public String formatted_address;

        public Geometry geometry;

        public String place_id;

        public List<String> types;
    }

    public static class AddressComponents {

        public String long_name;

        public String short_name;

        public List<String> types;
    }

    public static class Geometry {

        public Location location;

        public String location_type;

        public Viewport viewport;
    }

    public static class Location {

        public String lat;

        public String lng;
    }

    public static class Viewport {

        public Northeast northeast;

        public Southwest southwest;
    }

    public static class Northeast {

        public String lat;

        public String lng;
    }

    public static class Southwest {

        public String lat;

        public String lng;
    }
}
