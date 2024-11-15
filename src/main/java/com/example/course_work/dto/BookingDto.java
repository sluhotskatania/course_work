package com.example.course_work.dto;

import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.CityEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import com.example.course_work.enums.TypeAccommodationEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Booking}
 */
public record BookingDto(long id, Date created, CityEnum city, Date bookingDate,
                         BookingStatusEnum status, PaymentStatusEnum paymentStatus, Double totalPrice, String notes,
                         long accommodationId, String accommodationName, String accommodationLocation,
                         TypeAccommodationEnum accommodationType, Double accommodationPricePerNight,
                         Integer accommodationAvailability, long clientId, String clientName, String clientSurname,
                         String clientEmail, String clientPhone, Date clientBirthDate) implements Serializable {
  }