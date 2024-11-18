package com.example.course_work.repository;

import com.example.course_work.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TourRepository extends JpaRepository <Tour, Long>, JpaSpecificationExecutor<Tour> {
}
