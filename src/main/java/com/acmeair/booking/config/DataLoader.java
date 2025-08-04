package com.acmeair.booking.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.acmeair.booking.model.Flight;
import com.acmeair.booking.service.FlightService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {
    
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final FlightService flightService;
    private final ObjectMapper objectMapper;

    public DataLoader(FlightService flightService, ObjectMapper objectMapper) {
        this.flightService = flightService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/flights.json");
        if (inputStream == null) {
            throw new FileNotFoundException("flights.json not found in classpath");
        }

        List<Flight> flights = objectMapper.readValue(inputStream, new TypeReference<List<Flight>>() {
        });
        flights.forEach(flightService::addFlight);
        log.info("{} flights loaded successfully", flights.size());
    }
}