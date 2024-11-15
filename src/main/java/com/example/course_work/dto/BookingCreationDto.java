package com.example.course_work.dto;

import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.CityEnum;
import com.example.course_work.enums.PaymentStatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Booking}
 */
public record BookingCreationDto(CityEnum city, Date bookingDate, BookingStatusEnum status,
                                 PaymentStatusEnum paymentStatus, Double totalPrice,
                                 String notes, long accommodationId, long clientId) implements Serializable {
}