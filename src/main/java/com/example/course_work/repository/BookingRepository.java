package com.example.course_work.repository;

import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookingRepository extends JpaRepository <Booking, Long>, JpaSpecificationExecutor<Booking> {
    Page<Booking> findAll(Pageable pageable);

    Page<Booking> findByClientId(Long clientId, Pageable pageable);
}
