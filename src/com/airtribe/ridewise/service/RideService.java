package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.EntityNotFoundException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.*;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;


public class RideService {

    private final DriverService driverService;
    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;

    private final Map<String, Ride> store = new LinkedHashMap<>();

    public RideService(DriverService driverService,
                       RideMatchingStrategy matchingStrategy,
                       FareStrategy fareStrategy) {
        this.driverService = driverService;
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
    }


    public Ride requestRide(Rider rider, double distanceKm, VehicleType requestedVehicleType)
            throws NoDriverAvailableException {

        var availableDrivers = driverService.findAvailable().stream()
                .filter(d -> d.getVehicleType() == requestedVehicleType)
                .collect(Collectors.toList());

        Driver driver = matchingStrategy.findDriver(rider, availableDrivers);

        if (driver == null) {
            throw new NoDriverAvailableException("No drivers are currently available for this ride.");
        }

        Ride ride = new Ride(IdGenerator.nextRideId(), rider, distanceKm);
        ride.assignDriver(driver);
        driverService.markUnavailable(driver);

        store.put(ride.getId(), ride);
        return ride;
    }

    public FareReceipt completeRide(String rideId) {
        Ride ride = findByIdOrThrow(rideId);

        double fare = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(rideId, fare);

        ride.complete(receipt);

        Driver driver = ride.getDriver();
        driver.incrementTrips();
        driverService.markAvailable(driver);

        return receipt;
    }


    public void cancelRide(String rideId) {
        Ride ride = findByIdOrThrow(rideId);

        if (ride.getDriver() != null) {
            driverService.markAvailable(ride.getDriver());
        }

        ride.cancel();                                   
    }

 
    public Optional<Ride> findById(String rideId) {
        return Optional.ofNullable(store.get(rideId));
    }

    public Collection<Ride> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    public List<Ride> findByStatus(RideStatus status) {
        return store.values().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }

    
    public String matchingStrategyLabel() {
        return matchingStrategy.label();
    }

    public String fareStrategyLabel() {
        return fareStrategy.label();
    }


    private Ride findByIdOrThrow(String rideId) {
        return Optional.ofNullable(store.get(rideId))
                .orElseThrow(() -> new EntityNotFoundException("Ride not found: " + rideId));
    }
}
