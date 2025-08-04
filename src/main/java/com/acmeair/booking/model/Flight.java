package com.acmeair.booking.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.acmeair.booking.enums.FlightStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private Long id;
    private String airline;
    private String flightNumber;
    private String origin;              // e.g. AKL
    private String destination;         // e.g. SYD
    private Instant departureTime;      // UTC timestamp
    private Instant arrivalTime;        // UTC timestamp
    private BigDecimal price;           // price e.g. $349.99
    private Integer duration;           // in minutes e.g. 90 minutes
    private FlightStatus flightStatus;  // e.g. available, cancelled, full
    private Integer seatsRemaining;     // Number of seats remaining
}
