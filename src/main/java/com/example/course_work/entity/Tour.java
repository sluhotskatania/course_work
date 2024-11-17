package com.example.course_work.entity;

import com.example.course_work.enums.TypeEnum;
import jakarta.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;
    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

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

    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @Column(name = "max_participants")
    private Integer maxParticipants;
}
