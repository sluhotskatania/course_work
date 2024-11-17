package com.example.course_work.repository;


import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Tour;
import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation ,Long> {
    List<Accommodation> findByType(TypeAccommodationEnum type);
    List<Accommodation> findByLocationContainingIgnoreCase(String location);

}
