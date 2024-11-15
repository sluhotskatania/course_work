package com.example.course_work.dto;

import com.example.course_work.enums.TypeAccommodationEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Accommodation}
 */
public record AccommodationDto(long id, Date created, long tourId, long bookingId, String name, String location, TypeAccommodationEnum type, Double pricePerNight, Integer availability) implements Serializable {
  }