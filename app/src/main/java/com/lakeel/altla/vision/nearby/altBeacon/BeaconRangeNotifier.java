package com.lakeel.altla.vision.nearby.altBeacon;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

abstract class BeaconRangeNotifier implements RangeNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconRangeNotifier.class);

    @Override
    public final void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.isEmpty()) {
            LOGGER.debug("Not found beacons.");
            return;
        }

        for (Beacon beacon : beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // EddystoneUid
                String namespaceId = beacon.getId1().toString().replace("0x", "");
                String instanceId = beacon.getId2().toString().replace("0x", "");
                String beaconId = namespaceId + instanceId;

                onEddystoneUidFound(beaconId);
            }
        }
    }

    abstract void onEddystoneUidFound(String beaconId);
}
