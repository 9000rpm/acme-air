package com.acmeair.booking.enums;

public enum BookingStatus {
    BOOKED("BOOKED"),
    CANCELED("CANCELED");

    private final String value;

    BookingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
