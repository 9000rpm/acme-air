package com.acmeair.booking;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.acmeair.booking.controller.FlightController;
import com.acmeair.booking.enums.FlightStatus;
import com.acmeair.booking.exception.ResourceNotFoundException;
import com.acmeair.booking.model.Flight;
import com.acmeair.booking.service.FlightService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = FlightController.class)
@AutoConfigureMockMvc
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    // ----------- GET /flights/{id} --------------

    @Test
    void getFlightById_shouldReturnFlight_whenExists() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAirline("Air New Zealand");
        flight.setFlightNumber("NZ101");
        flight.setOrigin("AKL");
        flight.setDestination("WLG");
        flight.setDepartureTime(Instant.parse("2025-08-10T02:00:00Z"));
        flight.setArrivalTime(Instant.parse("2025-08-10T03:30:00Z"));
        flight.setPrice(new BigDecimal("349.99"));
        flight.setDuration(90);
        flight.setFlightStatus(FlightStatus.AVAILABLE);
        flight.setSeatsRemaining(5);

        Mockito.when(flightService.getFlight(1L)).thenReturn(flight);

        mockMvc.perform(get("/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.flightNumber").value("NZ101"))
                .andExpect(jsonPath("$.airline").value("Air New Zealand"))
                .andExpect(jsonPath("$.origin").value("AKL"))
                .andExpect(jsonPath("$.destination").value("WLG"))
                .andExpect(jsonPath("$.departureTime").value("2025-08-10T02:00:00Z"))
                .andExpect(jsonPath("$.arrivalTime").value("2025-08-10T03:30:00Z"))
                .andExpect(jsonPath("$.price").value(349.99))
                .andExpect(jsonPath("$.duration").value(90))
                .andExpect(jsonPath("$.flightStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$.seatsRemaining").value(5));
    }

    @Test
    void getFlightById_shouldReturnNotFound_whenMissing() throws Exception {
        Mockito.when(flightService.getFlight(999L))
                .thenThrow(new ResourceNotFoundException("Flight not found with id: 999"));

        mockMvc.perform(get("/flights/999"))
                .andExpect(status().isNotFound());
    }

    // ----------- /flights?origin={}&destination={}&date={} --------------    

    @Test
    void searchFlights_shouldReturnMatchingFlights() throws Exception {
        Instant departure = Instant.parse("2025-08-15T00:00:00Z");
        Instant arrival = Instant.parse("2025-08-15T02:00:00Z");

        Flight flight = new Flight();
        flight.setId(2L);
        flight.setAirline("Jetstar");
        flight.setFlightNumber("JQ101");
        flight.setOrigin("AKL");
        flight.setDestination("SYD");
        flight.setDepartureTime(departure);
        flight.setArrivalTime(arrival);
        flight.setPrice(new BigDecimal("199.99"));
        flight.setDuration(120);
        flight.setFlightStatus(FlightStatus.AVAILABLE);
        flight.setSeatsRemaining(3);

        Mockito.when(flightService.searchFlights("AKL", "SYD", LocalDate.of(2025, 8, 15)))
                .thenReturn(List.of(flight));

        mockMvc.perform(get("/flights")
                .param("origin", "AKL")
                .param("destination", "SYD")
                .param("date", "2025-08-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("JQ101"))
                .andExpect(jsonPath("$[0].flightStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].price").value(199.99));
    }

    @Test
    void searchFlights_shouldReturnBadRequest_whenMissingParams() throws Exception {
        mockMvc.perform(get("/flights")
                .param("origin", "AKL"))  // destination and date missing
                .andExpect(status().isBadRequest());
    }
}
