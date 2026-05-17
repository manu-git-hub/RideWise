package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Optional<Driver> findDriver(Rider rider, List<Driver> drivers) {
        if (drivers == null || drivers.isEmpty()) return Optional.empty();

        return drivers.stream()
                .min(Comparator.comparingInt(Driver::getTotalTripsCompleted));
    }

    @Override
    public String label() {
        return "Least Active Driver";
    }
}
