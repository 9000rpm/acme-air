package com.acmeair.booking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.acmeair.booking.exception.ResourceNotFoundException;
import com.acmeair.booking.model.Flight;
import com.acmeair.booking.repository.FlightRepository;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void addFlight(Flight flight) {
        flightRepository.save(flight);
    }

    public Flight getFlight(Long id) {
        return flightRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Flight not found with id: " + id));
    }

    public List<Flight> searchFlights(String origin, String destination, LocalDate date) {
        return flightRepository.search(origin, destination, date);
    }
}
