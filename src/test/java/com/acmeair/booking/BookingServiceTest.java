package com.acmeair.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acmeair.booking.dto.BookingCreateDto;
import com.acmeair.booking.dto.BookingResponseDto;
import com.acmeair.booking.enums.BookingStatus;
import com.acmeair.booking.enums.FlightStatus;
import com.acmeair.booking.exception.NoSeatsAvailableException;
import com.acmeair.booking.exception.ResourceNotFoundException;
import com.acmeair.booking.mapper.BookingMapper;
import com.acmeair.booking.model.Booking;
import com.acmeair.booking.model.Flight;
import com.acmeair.booking.repository.BookingRepository;
import com.acmeair.booking.repository.FlightRepository;
import com.acmeair.booking.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking_shouldReserveSeat_andReturnResponse() {
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setSeatsRemaining(1);
        flight.setFlightStatus(FlightStatus.AVAILABLE);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setFlightId(flightId);

        Booking booking = new Booking();

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        doAnswer(invocation -> {
            booking.setBookingStatus(BookingStatus.BOOKED);
            return null;
        }).when(bookingMapper).createBookingFromDto(eq(dto), any(Booking.class));
        when(bookingRepository.save(any(Booking.class))).thenReturn(100L);

        BookingResponseDto response = bookingService.createBooking(dto);

        assertEquals(100L, response.getId());
        assertEquals(0, flight.getSeatsRemaining());
        assertEquals(FlightStatus.FULL, flight.getFlightStatus());
        verify(flightRepository).save(flight);
    }

    @Test
    void createBooking_shouldThrowException_whenNoSeatsAvailable() {
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setSeatsRemaining(0);
        flight.setFlightStatus(FlightStatus.FULL);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setFlightId(flightId);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        assertThrows(NoSeatsAvailableException.class, () -> {
            bookingService.createBooking(dto);
        });
    }

    @Test
    void createBooking_shouldThrowException_whenFlightNotFound() {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setFlightId(999L);

        when(flightRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.createBooking(dto);
        });
    }

    @Test
    void cancelBooking_shouldIncrementSeats_andUpdateFlightStatus() {
        Long flightId = 1L;
        Long bookingId = 10L;

        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setSeatsRemaining(0);
        flight.setFlightStatus(FlightStatus.FULL);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setFlightId(flightId);
        booking.setBookingStatus(BookingStatus.BOOKED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        bookingService.cancelBooking(bookingId);

        assertEquals(BookingStatus.CANCELED, booking.getBookingStatus());
        assertEquals(1, flight.getSeatsRemaining());
        assertEquals(FlightStatus.AVAILABLE, flight.getFlightStatus());
        verify(flightRepository).save(flight);
    }
}
