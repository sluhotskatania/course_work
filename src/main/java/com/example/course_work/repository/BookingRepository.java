package com.example.course_work.repository;

import com.example.course_work.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository <Booking, Long> {
    List<Booking> findByClientId(Long clientId);
}
