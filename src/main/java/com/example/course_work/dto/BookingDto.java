package com.example.course_work.dto;

import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.CityEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import com.example.course_work.enums.TypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Booking}
 */
public record BookingDto(long id, Date created, long clientId, String clientName, String clientSurname,
                         String clientEmail, String clientPhone, Date clientBirthDate, long tourId, String tourName,
                         Double tourPrice, Date tourDepartureDate, Date tourReturnDate, TypeEnum tourType,
                         CityEnum city, Date bookingDate, BookingStatusEnum status, PaymentStatusEnum paymentStatus,
                         Double totalPrice, String notes) implements Serializable {
}