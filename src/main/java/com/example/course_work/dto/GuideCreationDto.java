package com.example.course_work.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.example.course_work.entity.Guide}
 */
public record GuideCreationDto(String name, String surname, String email, String phone, String languages,
                               Integer experience, Double rating) implements Serializable {
}