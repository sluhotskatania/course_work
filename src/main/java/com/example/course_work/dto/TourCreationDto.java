package com.example.course_work.dto;

import com.example.course_work.enums.TypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Tour}
 */
public record TourCreationDto(String name, String destination, Integer duration, Double price, Date departureDate,
                              Date returnDate, TypeEnum type, Integer maxParticipants, long accommodationId, long bookingId) implements Serializable {
}