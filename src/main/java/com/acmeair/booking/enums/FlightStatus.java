package com.acmeair.booking.enums;

public enum FlightStatus {
    AVAILABLE("AVAILABLE"),
    CANCELED("CANCELED"),
    FULL("FULL");

    private final String value;

    FlightStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
