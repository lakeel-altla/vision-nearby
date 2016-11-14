package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.resource.Geocode;

import java.util.List;

public final class LocationTextMapper {

    private static final String ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";

    private static final String LOCALITY = "locality";

    private static final String SUB_LOCALITY = "sublocality";

    public String map(Geocode entity) {
        String locationText = null;

        List<Geocode.Result> results = entity.results;
        for (Geocode.Result result : results) {
            List<Geocode.AddressComponents> address_components = result.address_components;

            String administrationAreaLevel1Location = null;
            String localityLocation = null;
            String subLocalityLocation = null;

            boolean isAdministrationAreaLevel1Included = false;
            boolean isLocalityIncluded = false;
            boolean isSubLocalityIncluded = false;

            for (Geocode.AddressComponents addressComponent : address_components) {
                List<String> types = addressComponent.types;
                for (String type : types) {
                    if (ADMINISTRATIVE_AREA_LEVEL_1.equals(type)) {
                        isAdministrationAreaLevel1Included = true;
                        administrationAreaLevel1Location = addressComponent.long_name;
                    }
                    if (LOCALITY.equals(type)) {
                        isLocalityIncluded = true;
                        localityLocation = addressComponent.long_name;
                    }
                    if (SUB_LOCALITY.equals(type)) {
                        isSubLocalityIncluded = true;
                        subLocalityLocation = addressComponent.long_name;
                    }
                }
                if (isAdministrationAreaLevel1Included && isLocalityIncluded && isSubLocalityIncluded) {
                    break;
                }
            }

            if (isAdministrationAreaLevel1Included && isLocalityIncluded && isSubLocalityIncluded) {
                // Append location string.
                locationText = administrationAreaLevel1Location + " " + localityLocation + " " + subLocalityLocation;
                break;
            }
        }

        return locationText;
    }
}
