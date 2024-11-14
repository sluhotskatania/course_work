package com.example.course_work.repository;

import com.example.course_work.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository <Guide, Long> {
}
