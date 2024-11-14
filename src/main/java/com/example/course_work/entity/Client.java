package com.example.course_work.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "clients")
@Getter
@Setter
@ToString

public class Client extends BaseEntity {
@Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Temporal(TemporalType.DATE)
    @Column (name = "birth_date")

    private Date birthDate;
}
