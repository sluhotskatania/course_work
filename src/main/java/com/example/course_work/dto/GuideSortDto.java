package com.example.course_work.dto;

import com.example.course_work.enums.LanguagesEnum;

import java.io.Serializable;
import java.util.Date;

public record GuideSortDto (Long id, Date created, String name, String surname, String email, String phone, LanguagesEnum languages,
                           Integer experience, Double rating) implements Serializable {
}
