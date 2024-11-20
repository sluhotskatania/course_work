package com.example.course_work.dto;

import com.example.course_work.enums.TypeEnum;

import java.io.Serializable;
import java.util.Date;

public record TourSortDto(Long id, Date created, String name, String destination, Integer duration, Double price,
                              Date departureDate, Date returnDate, TypeEnum type, Integer maxParticipants) implements Serializable {
}
