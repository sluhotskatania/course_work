package com.example.course_work.repository;

import com.example.course_work.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourGuideRepository extends JpaRepository <Accommodation, Long> {
}
