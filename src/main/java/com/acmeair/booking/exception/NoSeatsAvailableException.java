package com.acmeair.booking.exception;

public class NoSeatsAvailableException extends RuntimeException {
    public NoSeatsAvailableException(Long flightId) {
        super("No seats available for flight with ID: " + flightId);
    }
}

