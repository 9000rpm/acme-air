package com.acmeair.booking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acmeair.booking.model.Flight;
import com.acmeair.booking.service.FlightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/flights")
@Tag(name = "Flight API", description = "Operations related to flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(summary = "Get flight by ID", description = "Returns a single flight given its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Flight found", content = @Content(schema = @Schema(implementation = Flight.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlight(id));
    }

    @Operation(summary = "Search flights", description = "Search for flights by origin, destination, and date", responses = {
            @ApiResponse(responseCode = "200", description = "List of matching flights", content = @Content(schema = @Schema(implementation = Flight.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping
    public ResponseEntity<List<Flight>> searchFlights(
            @Parameter(description = "Origin airport code", required = true) @RequestParam @NotBlank String origin,
            @Parameter(description = "Destination airport code", required = true) @RequestParam @NotBlank String destination,
            @Parameter(description = "Date of the flight (ISO format yyyy-MM-dd)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(flightService.searchFlights(origin, destination, date));
    }
}
