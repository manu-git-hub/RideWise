package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.*;

public class RiderService {

    private final Map<String, Rider> store = new LinkedHashMap<>();

    public Rider register(String name, String location) {
        Rider rider = new Rider(IdGenerator.nextRiderId(), name.trim(), location.trim());
        store.put(rider.getId(), rider);
        return rider;
    }

    public Optional<Rider> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Collection<Rider> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }
}
