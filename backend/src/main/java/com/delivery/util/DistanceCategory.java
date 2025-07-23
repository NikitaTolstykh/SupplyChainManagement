package com.delivery.util;

public enum DistanceCategory {
    SHORT("Until 10 km"),
    MEDIUM("10-50 km"),
    LONG("More than 50 км");
    private final String description;

    DistanceCategory(String description) {
        this.description = description;
    }
}
