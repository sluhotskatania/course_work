package com.example.course_work.dto;

import com.example.course_work.enums.TypeAccommodationEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.example.course_work.entity.Accommodation}
 */
public record AccommodationCreationDto(
        @NotNull(message = "Accommodation name cannot be null")
        @Size(min = 2, max = 100, message = "Accommodation name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Location cannot be null")
        @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
        String location,

        @NotNull(message = "Accommodation type cannot be null")
        TypeAccommodationEnum type,

        @NotNull(message = "Price per night cannot be null")
        @Positive(message = "Price per night must be a positive value")
        Double pricePerNight,

        @NotNull(message = "Availability cannot be null")
        @Min(value = 0, message = "Availability cannot be negative")
        Integer availability
) implements Serializable {
  }