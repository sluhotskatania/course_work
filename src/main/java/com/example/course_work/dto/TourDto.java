package com.example.course_work.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Tour}
 */
public record TourDto(long id, Date created, String name, String destination, Integer duration, Double price,
                      Date departureDate, Date returnDate, String type,
                      Integer maxParticipants) implements Serializable {
}