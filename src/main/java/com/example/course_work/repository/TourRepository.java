package com.example.course_work.repository;

import com.example.course_work.entity.Tour;
import com.example.course_work.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository <Tour, Long> {
    List<Tour> findByType(TypeEnum type);

    List<Tour> findByDestinationContainingIgnoreCase(String destination);
    List<Tour> findAllByOrderByDepartureDateAsc();
    List<Tour> findAllByOrderByDepartureDateDesc();
    List<Tour> findAllByOrderByPriceAsc();
    List<Tour> findAllByOrderByPriceDesc();
    List<Tour> findAllByOrderByDurationAsc();
    List<Tour> findAllByOrderByDurationDesc();
}
