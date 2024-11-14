package com.example.course_work.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Client}
 */
public record ClientCreationDto(@NotBlank String name, @NotBlank String surname, @Email @NotBlank String email, @Size(max = 10) @NotBlank String phone, @NotNull @Past Date birthDate) implements Serializable {
  }