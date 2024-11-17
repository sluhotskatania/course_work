package com.example.course_work.dto;

import com.example.course_work.enums.LanguagesEnum;
import com.example.course_work.enums.TypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Guide}
 */
public record GuideDto(long id, Date created, String name, String surname, String email, String phone, LanguagesEnum languages,
                       Integer experience, Double rating, long tourId, String tourName, String tourDestination,
                       Integer tourDuration, Date tourDepartureDate, Date tourReturnDate, TypeEnum tourType) implements Serializable {
}