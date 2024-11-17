package com.example.course_work.dto;

import java.io.Serializable;
import java.util.Date;
/**
 * DTO for {@link com.example.course_work.entity.Client}
 */
public record ClientDto(long id, Date created, String name, String surname, String email, String phone,
                        Date birthDate) implements Serializable {
}