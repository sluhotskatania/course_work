package com.example.course_work.entity;

import com.example.course_work.enums.TypeAccommodationEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "accommodation ")
@Getter
@Setter
@ToString
public class Accommodation extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "name")
    private String name;
    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeAccommodationEnum type;

    @Column(name = "price_per_night")
    private Double pricePerNight;
    @Column(name = "availability")
    private Integer availability;

}
