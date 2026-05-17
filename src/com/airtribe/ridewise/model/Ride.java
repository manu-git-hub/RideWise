package com.airtribe.ridewise.model;

import java.util.Objects;


public class Ride {

    private final String id;
    private final Rider rider;
    private final double distanceKm;
    private Driver driver;
    private RideStatus status;
    private FareReceipt fareReceipt;

    public Ride(String id, Rider rider, double distanceKm) {
        this.id = Objects.requireNonNull(id);
        this.rider = Objects.requireNonNull(rider);
        this.distanceKm = distanceKm;
        this.status = RideStatus.REQUESTED;
    }


    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public RideStatus getStatus() {
        return status;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }


    public void assignDriver(Driver driver) {
        requireStatus(RideStatus.REQUESTED, "assign driver");
        this.driver = Objects.requireNonNull(driver);
        this.status = RideStatus.ASSIGNED;
    }

    public void complete(FareReceipt receipt) {
        requireStatus(RideStatus.ASSIGNED, "complete");
        this.fareReceipt = Objects.requireNonNull(receipt);
        this.status = RideStatus.COMPLETED;
    }

    public void cancel() {
        if (status == RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed ride: " + id);
        }
        this.status = RideStatus.CANCELLED;
    }


    private void requireStatus(RideStatus expected, String operation) {
        if (status != expected) {
            throw new IllegalStateException(
                    String.format("Cannot %s ride %s — current status: %s (expected: %s)",
                            operation, id, status, expected));
        }
    }

    @Override
    public String toString() {
        String driverName = (driver != null) ? driver.getName() : "unassigned";
        return String.format("[%s] rider=%-14s  driver=%-14s  dist=%5.1f km  status=%s",
                id, rider.getName(), driverName, distanceKm, status);
    }
}
