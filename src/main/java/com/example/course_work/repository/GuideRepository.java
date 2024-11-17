package com.example.course_work.repository;

import com.example.course_work.entity.Guide;
import com.example.course_work.enums.LanguagesEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideRepository extends JpaRepository <Guide, Long> {
    List<Guide> findByLanguages(LanguagesEnum languages);
    List<Guide> findAllByOrderByLanguagesAsc();
    List<Guide> findAllByOrderByRatingDesc();
}
