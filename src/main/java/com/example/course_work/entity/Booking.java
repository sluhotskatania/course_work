package com.example.course_work.entity;

import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.CityEnum;
import com.example.course_work.enums.PaymentStatusEnum;
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
    private Client client;
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    @ManyToOne
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;
    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private CityEnum city;

    @Temporal(TemporalType.DATE)
    @Column(name = "booking_date")
    private Date bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatusEnum paymentStatus;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "notes")
    private String notes;
}
