package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {

    private final DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
    private static final double SURGE_MULTIPLIER = 1.5;

    private record TimeWindow(LocalTime start, LocalTime end) {
        boolean contains(LocalTime t) {
            return !t.isBefore(start) && t.isBefore(end);
        }
    }

    private static final TimeWindow[] PEAK_WINDOWS = {
            new TimeWindow(LocalTime.of(8, 0), LocalTime.of(10, 0)),
            new TimeWindow(LocalTime.of(17, 0), LocalTime.of(20, 0))
    };

    @Override
    public double calculateFare(Ride ride) {
        double base = defaultFareStrategy.calculateFare(ride);
        return isPeakHour() ? base * SURGE_MULTIPLIER : base;
    }

    @Override
    public String label() {
        return defaultFareStrategy.label() + String.format(
                " with Peak-Hour %.1fx surge (08-10 & 17-20)", SURGE_MULTIPLIER);
    }

    private boolean isPeakHour() {
        LocalTime now = LocalTime.now();
        for (TimeWindow w : PEAK_WINDOWS) {
            if (w.contains(now)) return true;
        }
        return false;
    }
}
