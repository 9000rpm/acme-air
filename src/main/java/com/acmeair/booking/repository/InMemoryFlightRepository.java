package com.acmeair.booking.repository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.acmeair.booking.model.Flight;

@Repository
public class InMemoryFlightRepository implements FlightRepository {

    private final Map<Long, Flight> flights = new ConcurrentHashMap<>();

    @Override
    public void save(Flight flight) {
        if (flight.getId() == null) {
            throw new IllegalArgumentException("Flight ID cannot be null");
        }
        flights.put(flight.getId(), flight);
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return Optional.ofNullable(flights.get(id));
    }

    @Override
    public List<Flight> search(String origin, String destination, LocalDate date) {
        return flights.values().stream()
                .filter(f -> f.getOrigin().equalsIgnoreCase(origin) &&
                        f.getDestination().equalsIgnoreCase(destination) &&
                        f.getDepartureTime().atZone(ZoneOffset.UTC).toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
}
