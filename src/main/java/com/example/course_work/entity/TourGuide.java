package com.example.course_work.entity;

import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "tour_guide")
@Getter
@Setter
@ToString
public class TourGuide extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    @Column(name = "tour")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "guide_id", nullable = false)
    @Column(name = "guide")
    private Guide guide;

    @Temporal(TemporalType.DATE)
    @Column(name = "assigned_date")
    private Date assignedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
}
