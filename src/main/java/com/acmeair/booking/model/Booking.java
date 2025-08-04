package com.acmeair.booking.model;

import java.time.LocalDate;

import com.acmeair.booking.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private Long id;
    private Long flightId;
    private String title;
    private String firstName;
    private String familyName;
    private LocalDate dob;
    private String frequentFlyerProgramme;
    private String membershipNumber;
    private String mobile;
    private String email;
    private BookingStatus bookingStatus;
}
