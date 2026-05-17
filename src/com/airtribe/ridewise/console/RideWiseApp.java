package com.airtribe.ridewise.console;

import com.airtribe.ridewise.exception.EntityNotFoundException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;

public class RideWiseApp {

    private final RiderService riderService;
    private final DriverService driverService;
    private final RideService rideService;
    private final ConsoleInput input;

    public RideWiseApp(RiderService riderService,
            DriverService driverService,
            RideService rideService,
            ConsoleInput input) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.rideService = rideService;
        this.input = input;
    }

    public void run() {
        ConsoleOutput.printBanner(
                rideService.matchingStrategyLabel(),
                rideService.fareStrategyLabel());

        boolean running = true;
        while (running) {
            ConsoleOutput.printMenu();
            int choice = input.readInt("  Choice: ", 1, 8);
            switch (choice) {
                case 1 -> handleAddRider();
                case 2 -> handleAddDriver();
                case 3 -> handleViewAvailableDrivers();
                case 4 -> handleRequestRide();
                case 5 -> handleCompleteRide();
                case 6 -> handleCancelRide();
                case 7 -> handleViewAllRides();
                case 8 -> running = false;
            }
        }

        ConsoleOutput.printGoodbye();
    }

    private void handleAddRider() {
        ConsoleOutput.sectionHeader("Add Rider");
        String name = input.readNonBlank("  Name    : ");
        String location = input.readNonBlank("  Location: ");
        Rider rider = riderService.register(name, location);
        ConsoleOutput.success("Rider registered — " + rider);
    }

    private void handleAddDriver() {
        ConsoleOutput.sectionHeader("Add Driver");
        String name = input.readNonBlank("  Name    : ");
        String location = input.readNonBlank("  Location: ");

        System.out.println("  Select Vehicle Type:");
        System.out.println("    1. BIKE");
        System.out.println("    2. AUTO");
        System.out.println("    3. CAR");
        int vtChoice = input.readInt("  Vehicle [1-3]: ", 1, 3);
        VehicleType vt = VehicleType.values()[vtChoice - 1];

        var driver = driverService.register(name, location, vt);
        ConsoleOutput.success("Driver registered — " + driver);
    }

    private void handleViewAvailableDrivers() {
        ConsoleOutput.sectionHeader("Available Drivers");
        ConsoleOutput.printDrivers(driverService.findAvailable());
    }

    private void handleRequestRide() {
        ConsoleOutput.sectionHeader("Request a Ride");

        if (riderService.isEmpty()) {
            ConsoleOutput.error("No riders registered — add a rider first.");
            return;
        }

        ConsoleOutput.info("Registered riders:");
        ConsoleOutput.printRiders(riderService.findAll());

        String riderId = input.readNonBlank("  Rider ID (e.g. RDR-001): ").toUpperCase();
        Rider rider = riderService.findById(riderId).orElse(null);
        if (rider == null) {
            ConsoleOutput.error("Rider not found: " + riderId);
            return;
        }

        System.out.println("  Select Requested Vehicle Type:");
        System.out.println("    1. BIKE");
        System.out.println("    2. AUTO");
        System.out.println("    3. CAR");
        int vtChoice = input.readInt("  Vehicle [1-3]: ", 1, 3);
        VehicleType requestedType = VehicleType.values()[vtChoice - 1];

        double distance = input.readDouble("  Distance (km): ", 0.1, 500.0);

        try {
            Ride ride = rideService.requestRide(rider, distance, requestedType);
            ConsoleOutput.success("Ride booked — " + ride);
        } catch (NoDriverAvailableException e) {
            ConsoleOutput.error(e.getMessage());
        }
    }

    private void handleCompleteRide() {
        ConsoleOutput.sectionHeader("Complete a Ride");

        var assignedRides = rideService.findByStatus(RideStatus.ASSIGNED);
        if (assignedRides.isEmpty()) {
            ConsoleOutput.error("No rides are currently ASSIGNED.");
            return;
        }

        ConsoleOutput.info("Rides awaiting completion:");
        ConsoleOutput.printRides(assignedRides);

        String rideId = input.readNonBlank("  Ride ID (e.g. RIDE-0001): ").toUpperCase();
        try {
            FareReceipt receipt = rideService.completeRide(rideId);
            ConsoleOutput.success("Ride completed.");
            ConsoleOutput.printReceipt(receipt);
        } catch (EntityNotFoundException | IllegalStateException e) {
            ConsoleOutput.error(e.getMessage());
        }
    }

    private void handleCancelRide() {
        ConsoleOutput.sectionHeader("Cancel a Ride");

        var cancellable = rideService.findByStatus(RideStatus.REQUESTED);
        cancellable.addAll(rideService.findByStatus(RideStatus.ASSIGNED));

        if (cancellable.isEmpty()) {
            ConsoleOutput.error("No rides available to cancel.");
            return;
        }

        ConsoleOutput.info("Cancellable rides:");
        ConsoleOutput.printRides(cancellable);

        String rideId = input.readNonBlank("  Ride ID (e.g. RIDE-0001): ").toUpperCase();
        try {
            rideService.cancelRide(rideId);
            ConsoleOutput.success("Ride " + rideId + " cancelled.");
        } catch (EntityNotFoundException | IllegalStateException e) {
            ConsoleOutput.error(e.getMessage());
        }
    }

    private void handleViewAllRides() {
        ConsoleOutput.sectionHeader("All Rides");
        ConsoleOutput.printRides(rideService.findAll());
    }
}
