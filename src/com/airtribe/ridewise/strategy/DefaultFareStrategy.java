package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 15.0;
    private static final double RATE_PER_KM = 12.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + (RATE_PER_KM * ride.getDistanceKm());
    }

    @Override
    public String label() {
        return String.format("Default  (Rs %.0f base + Rs %.0f/km)", BASE_FARE, RATE_PER_KM);
    }
}
