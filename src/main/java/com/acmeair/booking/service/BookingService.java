package com.acmeair.booking.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acmeair.booking.config.DataLoader;
import com.acmeair.booking.dto.BookingCreateDto;
import com.acmeair.booking.dto.BookingResponseDto;
import com.acmeair.booking.dto.BookingUpdateDto;
import com.acmeair.booking.enums.BookingStatus;
import com.acmeair.booking.enums.FlightStatus;
import com.acmeair.booking.exception.NoSeatsAvailableException;
import com.acmeair.booking.exception.ResourceNotFoundException;
import com.acmeair.booking.mapper.BookingMapper;
import com.acmeair.booking.model.Booking;
import com.acmeair.booking.model.Flight;
import com.acmeair.booking.repository.BookingRepository;
import com.acmeair.booking.repository.FlightRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final BookingMapper bookingMapper;
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository,
            BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.bookingMapper = bookingMapper;
    }

    public BookingResponseDto createBooking(BookingCreateDto bookingCreateDto) {
        // check and reserve seats
        updateSeatsRemaining(bookingCreateDto.getFlightId(), BookingStatus.BOOKED);

        // Create booking
        log.info("Creating booking for flight {}", bookingCreateDto.getFlightId());
        Booking booking = new Booking();
        bookingMapper.createBookingFromDto(bookingCreateDto, booking);
        booking.setBookingStatus(BookingStatus.BOOKED);
        Long bookingId = bookingRepository.save(booking);
        return new BookingResponseDto(bookingId);
    }

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public void updateBooking(Long id, BookingUpdateDto bookingUpdateDto) {
        Booking booking = getBooking(id);
        bookingMapper.updateBookingFromDto(bookingUpdateDto, booking);
        bookingRepository.save(booking);
    }

    /**
     * Cancels a booking if not already canceled and updates flight seats remaining.
     * This method is idempotent: cancelling an already canceled booking is a no-op.
     */
    public void cancelBooking(Long id) {
        Booking booking = getBooking(id);

        if (BookingStatus.CANCELED.equals(booking.getBookingStatus())) {
            return;
        }

        // Cancel booking
        log.info("Cancel booking {}", booking.getId());
        booking.setBookingStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);

        // Increment seats remaining
        updateSeatsRemaining(booking.getFlightId(), BookingStatus.CANCELED);
    }

    // Update seats remaining
    private void updateSeatsRemaining(Long id, BookingStatus bookingStatus) {
        // Check flight exists
        Flight flight = flightRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Flight not found with id: " + id));

        if (BookingStatus.BOOKED.equals(bookingStatus)) {
            if (flight.getSeatsRemaining() > 0) {
                flight.setSeatsRemaining(flight.getSeatsRemaining() - 1); // reduce seats remaining
                // check if flight is full
                if (flight.getSeatsRemaining() == 0) {
                    flight.setFlightStatus(FlightStatus.FULL);
                }
            } else {
                log.warn("No seats left on flight {}", flight.getId());
                throw new NoSeatsAvailableException(flight.getId());
            }
        } else if (BookingStatus.CANCELED.equals(bookingStatus)) {
            flight.setSeatsRemaining(flight.getSeatsRemaining() + 1); // increment seats remaining
            // Update flightStatus if full
            if (flight.getFlightStatus().equals(FlightStatus.FULL)) {
                flight.setFlightStatus(FlightStatus.AVAILABLE);
            }
        }
        flightRepository.save(flight);
    }
}
