package com.example.course_work.repository;


import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Tour;
import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.enums.TypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation ,Long>, JpaSpecificationExecutor<Accommodation> {
    Page<Accommodation> findByType(TypeAccommodationEnum type, Pageable pageable);

    Page<Accommodation> findByLocationContainingIgnoreCase(String location, Pageable pageable);

}
