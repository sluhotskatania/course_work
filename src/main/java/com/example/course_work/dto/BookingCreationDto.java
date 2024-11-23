package com.example.course_work.dto;

import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Booking}
 */
public record BookingCreationDto(
        @NotNull(message = "Booking date cannot be null")
        @PastOrPresent(message = "Booking date must be in the past or present")
        Date bookingDate,

        @NotNull(message = "Booking status cannot be null")
        BookingStatusEnum status,

        @NotNull(message = "Payment status cannot be null")
        PaymentStatusEnum paymentStatus,

        @NotNull(message = "Total price cannot be null")
        @Positive(message = "Total price must be a positive value")
        Double totalPrice,

        @Size(max = 500, message = "Notes must be less than 500 characters")
        String notes,

        @Min(value = 1, message = "Number of nights must be at least 1")
        @Max(value = 365, message = "Number of nights cannot exceed 365")
        int nights,

        @NotNull(message = "Accommodation ID cannot be null")
        long accommodationId,

        @NotNull(message = "Client ID cannot be null")
        long clientId
)  implements Serializable {
}