package com.acmeair.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acmeair.booking.dto.BookingCreateDto;
import com.acmeair.booking.dto.BookingResponseDto;
import com.acmeair.booking.dto.BookingUpdateDto;
import com.acmeair.booking.model.Booking;
import com.acmeair.booking.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking", description = "APIs for managing flight bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Get all bookings")
    @ApiResponse(responseCode = "200", description = "List of bookings retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Booking>> getBookings() {
        return ResponseEntity.ok(bookingService.getBookings());
    }

    @Operation(summary = "Create a new booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking created successfully", content = @Content(schema = @Schema(implementation = BookingResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "No seats available", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingCreateDto bookingCreateDto) {
        BookingResponseDto bookingResponseDto = bookingService.createBooking(bookingCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponseDto);
    }

    @Operation(summary = "Update an existing booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBooking(@Parameter(description = "Booking ID") @PathVariable Long id,
            @Valid @RequestBody BookingUpdateDto bookingUpdateDto) {
        bookingService.updateBooking(id, bookingUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancel an existing booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

}
