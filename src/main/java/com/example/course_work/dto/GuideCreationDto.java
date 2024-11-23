package com.example.course_work.dto;

import com.example.course_work.enums.LanguagesEnum;
import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link com.example.course_work.entity.Guide}
 */
public record GuideCreationDto(
        @NotNull(message = "Name cannot be null")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotNull(message = "Surname cannot be null")
        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
        String surname,

        @NotNull(message = "Email cannot be null")
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Phone cannot be null")
        @Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits")
        String phone,

        @NotNull(message = "Languages cannot be null")
        LanguagesEnum languages,

        @NotNull(message = "Experience cannot be null")
        @Min(value = 0, message = "Experience cannot be negative")
        @Max(value = 50, message = "Experience cannot exceed 50 years")
        Integer experience,

        @NotNull(message = "Rating cannot be null")
        @Min(value = 0, message = "Rating cannot be less than 0")
        @Max(value = 5, message = "Rating cannot exceed 5")
        Double rating,

        @NotNull(message = "Tour ID cannot be null")
        long tourId
) implements Serializable {
}