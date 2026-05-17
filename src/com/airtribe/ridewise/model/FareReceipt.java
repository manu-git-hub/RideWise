package com.airtribe.ridewise.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class FareReceipt {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    private final String rideId;
    private final double amount;
    private final LocalDateTime generatedAt;

    public FareReceipt(String rideId, double amount) {
        this.rideId = rideId;
        this.amount = amount;
        this.generatedAt = LocalDateTime.now();
    }

    public String getRideId() {
        return rideId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return String.format("Receipt{ride=%s  fare=Rs %.2f  at=%s}",
                rideId, amount, generatedAt.format(FMT));
    }
}
