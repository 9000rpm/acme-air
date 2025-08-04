package com.acmeair.booking.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Error response model")
public class ErrorResponse {
    @Schema(description = "Error code identifier", example = "FLIGHT_NOT_FOUND")
    private String errorCode;

    @Schema(description = "Detailed error message", example = "Flight not found with id: 123")
    private String message;
}
