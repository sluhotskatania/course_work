package com.example.course_work.entity;

import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "booking")
@Getter
@Setter
@ToString
public class Booking extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @Column(name = "client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    @Column(name = "tour")
    private Tour tour;

    @Temporal(TemporalType.DATE)
    @Column(name = "booking_date")
    private Date bookingDate;
    @Column(name = "status")
    private String status;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "notes")
    private String notes;
}
