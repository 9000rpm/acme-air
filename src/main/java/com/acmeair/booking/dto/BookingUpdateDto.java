package com.acmeair.booking.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateDto {
    @NotBlank
    private String title;
    @NotBlank
    private String firstName;
    @NotBlank
    private String familyName;
    @NotNull
    private LocalDate dob;
    private String frequentFlyerProgramme;
    private String membershipNumber;
    @NotBlank
    private String mobile;
    @NotBlank
    private String email;
}
