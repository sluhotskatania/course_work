package com.example.course_work.dto;

import com.example.course_work.enums.LanguagesEnum;

import java.io.Serializable;

/**
 * DTO for {@link com.example.course_work.entity.Guide}
 */
public record GuideCreationDto(String name, String surname, String email, String phone, LanguagesEnum languages,
                               Integer experience, Double rating, long tourId) implements Serializable {

}