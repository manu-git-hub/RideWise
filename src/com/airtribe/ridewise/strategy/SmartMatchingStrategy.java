package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;


public class SmartMatchingStrategy implements RideMatchingStrategy {

    private final NearestDriverStrategy nearestStrategy = new NearestDriverStrategy();
    private final LeastActiveDriverStrategy leastActiveStrategy = new LeastActiveDriverStrategy();

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        if (drivers == null || drivers.isEmpty()) {
            return null;
        }

        Driver nearestDriver = nearestStrategy.findDriver(rider, drivers);

        if (nearestDriver != null && nearestDriver.getTotalTripsCompleted() >= 3) {
            return leastActiveStrategy.findDriver(rider, drivers);
        }

        return nearestDriver;
    }

    @Override
    public String label() {
        return "Smart Dynamic Matching (Nearest + LeastActive Fallback)";
    }
}
