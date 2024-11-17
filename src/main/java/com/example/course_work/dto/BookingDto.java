package com.example.course_work.dto;

import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import com.example.course_work.enums.TypeAccommodationEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Booking}
 */
public record BookingDto(long id, Date created, Date bookingDate,
                         BookingStatusEnum status, PaymentStatusEnum paymentStatus, Double totalPrice, String notes,
                         long accommodationId, String accommodationName, String accommodationLocation,
                         long clientId, String clientName, String clientSurname,
                         String clientEmail, String clientPhone, int nights) implements Serializable {
  }