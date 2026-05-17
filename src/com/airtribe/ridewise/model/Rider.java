package com.airtribe.ridewise.model;

import java.util.Objects;

public class Rider {

    private final String id;
    private final String name;
    private String location;

    public Rider(String id, String name, String location) {
        this.id = Objects.requireNonNull(id, "Rider id must not be null");
        this.name = Objects.requireNonNull(name, "Rider name must not be null");
        this.location = Objects.requireNonNull(location, "Rider location must not be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Objects.requireNonNull(location);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s  @%s", id, name, location);
    }
}
