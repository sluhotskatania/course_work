package com.example.course_work.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Tour}
 */
public record TourCreationDto(String name, String destination, Integer duration, Double price, Date departureDate,
                              Date returnDate, String type, Integer maxParticipants) implements Serializable {
}