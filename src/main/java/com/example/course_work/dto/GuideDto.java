package com.example.course_work.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.course_work.entity.Guide}
 */
public record GuideDto(long id, Date created, String name, String surname, String email, String phone, String languages,
                       Integer experience, Double rating) implements Serializable {
}