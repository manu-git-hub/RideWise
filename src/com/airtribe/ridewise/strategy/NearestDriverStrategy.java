package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Optional<Driver> findDriver(Rider rider, List<Driver> drivers) {
        if (drivers == null || drivers.isEmpty()) return Optional.empty();

        String riderLoc = rider.getLocation().toLowerCase();

        return drivers.stream()
                .min(Comparator.comparingInt(
                        d -> locationDistance(riderLoc, d.getCurrentLocation().toLowerCase())));
    }

    @Override
    public String label() {
        return "Nearest Driver";
    }

    private int locationDistance(String a, String b) {
        int common = 0;
        int limit = Math.min(a.length(), b.length());
        while (common < limit && a.charAt(common) == b.charAt(common)) common++;
        return (a.length() - common) + (b.length() - common);
    }
}
