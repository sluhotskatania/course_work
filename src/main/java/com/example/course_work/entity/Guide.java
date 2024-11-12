package com.example.course_work.entity;

import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "guide")
@Getter
@Setter
@ToString
public class Guide extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "languages")
    private String languages;
    @Column(name = "experience")
    private Integer experience;
    @Column(name = "rating")
    private Double rating;

}
