package com.acmeair.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.acmeair.booking.controller.BookingController;
import com.acmeair.booking.dto.BookingCreateDto;
import com.acmeair.booking.dto.BookingResponseDto;
import com.acmeair.booking.dto.BookingUpdateDto;
import com.acmeair.booking.enums.BookingStatus;
import com.acmeair.booking.exception.ResourceNotFoundException;
import com.acmeair.booking.model.Booking;
import com.acmeair.booking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    // ----------- GET /bookings --------------

    @Test
    void getBookings_shouldReturnListOfBookings() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setFlightId(100L);
        booking.setFirstName("John");
        booking.setFamilyName("Doe");
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setDob(LocalDate.of(1985, 5, 20));
        booking.setEmail("john.doe@example.com");
        booking.setMobile("123456789");

        Mockito.when(bookingService.getBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].bookingStatus").value("BOOKED"));
    }

    @Test
    void getBookings_shouldReturnEmptyList_whenNoBookings() throws Exception {
        Mockito.when(bookingService.getBookings()).thenReturn(List.of());

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ----------- POST /bookings -------------

    @Test
    void createBooking_shouldReturnCreatedBookingId_whenValidRequest() throws Exception {
        BookingCreateDto requestDto = new BookingCreateDto(
                100L, "Mr", "John", "Doe", LocalDate.of(1985, 5, 20),
                "FrequentFlyer", "FF12345", "123456789", "john.doe@example.com");

        BookingResponseDto responseDto = new BookingResponseDto(1L);

        Mockito.when(bookingService.createBooking(Mockito.any())).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createBooking_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        BookingCreateDto invalidDto = new BookingCreateDto(); // missing required fields

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // ----------- PUT /bookings/{id} -------------

    @Test
    void updateBooking_shouldReturnNoContent_whenUpdateSucceeds() throws Exception {
        BookingUpdateDto updateDto = new BookingUpdateDto(
                "Mr", "John", "Doe", LocalDate.of(1985, 5, 20),
                "FrequentFlyer", "FF12345", "123456789", "john.doe@example.com");

        // no need to mock anything because service returns void and no exceptions

        mockMvc.perform(put("/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateBooking_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        BookingUpdateDto invalidDto = new BookingUpdateDto(); // missing fields

        mockMvc.perform(put("/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // ----------- DELETE /bookings/{id} -------------

    @Test
    void cancelBooking_shouldReturnNoContent_whenBookingExists() throws Exception {
        // Assume service cancels booking successfully, no exceptions thrown

        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelBooking_shouldReturnNotFound_whenBookingMissing() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Booking not found with id: 999"))
                .when(bookingService).cancelBooking(999L);

        mockMvc.perform(delete("/bookings/999"))
                .andExpect(status().isNotFound());
    }
}
