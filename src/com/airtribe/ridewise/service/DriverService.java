package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;


public class DriverService {

    private final Map<String, Driver> store = new LinkedHashMap<>();

    public Driver register(String name, String location, VehicleType vehicleType) {
        Driver driver = new Driver(IdGenerator.nextDriverId(), name.trim(), location.trim(), vehicleType);
        store.put(driver.getId(), driver);
        return driver;
    }

    public Optional<Driver> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Collection<Driver> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    public List<Driver> findAvailable() {
        return store.values().stream()
                .filter(Driver::isAvailable)
                .collect(Collectors.toList());
    }

    public void markUnavailable(Driver driver) {
        driver.setAvailable(false);
    }

    public void markAvailable(Driver driver) {
        driver.setAvailable(true);
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }
}
