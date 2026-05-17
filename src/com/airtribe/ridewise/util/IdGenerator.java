package com.airtribe.ridewise.util;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {

    private static final AtomicInteger riderSeq = new AtomicInteger(1);
    private static final AtomicInteger driverSeq = new AtomicInteger(1);
    private static final AtomicInteger rideSeq = new AtomicInteger(1);
    private IdGenerator() {
    }
    public static String nextRiderId() {
        return String.format("RDR-%03d", riderSeq.getAndIncrement());
    }

    public static String nextDriverId() {
        return String.format("DRV-%03d", driverSeq.getAndIncrement());
    }

    public static String nextRideId() {
        return String.format("RIDE-%04d", rideSeq.getAndIncrement());
    }
}
