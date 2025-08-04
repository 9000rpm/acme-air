package com.acmeair.booking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.acmeair.booking.model.Flight;

public interface FlightRepository {
    void save(Flight flight);

    Optional<Flight> findById(Long id);

    List<Flight> search(String origin, String destination, LocalDate date);
}
