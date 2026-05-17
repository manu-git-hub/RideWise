package com.airtribe.ridewise.model;

import java.util.Objects;

public class Driver {

    private final String id;
    private final String name;
    private final VehicleType vehicleType;
    private String currentLocation;
    private boolean available;
    private int totalTripsCompleted;

    public Driver(String id, String name, String currentLocation, VehicleType vehicleType) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.currentLocation = Objects.requireNonNull(currentLocation);
        this.vehicleType = Objects.requireNonNull(vehicleType);
        this.available = true;
        this.totalTripsCompleted = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getTotalTripsCompleted() {
        return totalTripsCompleted;
    }

    public void setCurrentLocation(String loc) {
        this.currentLocation = Objects.requireNonNull(loc);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void incrementTrips() {
        this.totalTripsCompleted++;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-16s [%s] @%-14s  available=%-5b  trips=%d",
                id, name, vehicleType, currentLocation, available, totalTripsCompleted);
    }
}
