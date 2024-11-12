package com.example.course_work.entity;

import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "tours")
@Getter
@Setter
@ToString

public class Tour extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "destination")
    private String destination;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "price")
    private Double price;

    @Temporal(TemporalType.DATE)
    @Column(name = "departure_date")
    private Date departureDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "return_date")
    private Date returnDate;
    @Column(name = "type")
    private String type;
    @Column(name = "max_participants")
    private Integer maxParticipants;
}
