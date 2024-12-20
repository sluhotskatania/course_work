package com.example.course_work.dto;

import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.enums.TypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Tour}
 */
public record TourDto(Long id, String name, String destination, Integer duration, Double price,
                      Date departureDate, Date returnDate, TypeEnum type,
                      Integer maxParticipants,  String accommodationName,
                      String accommodationLocation, TypeAccommodationEnum accommodationType
        ,Double bookingTotalPrice, String bookingNotes) implements Serializable {
}