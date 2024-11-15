package com.example.course_work.service;

import com.example.course_work.dto.GuideCreationDto;
import com.example.course_work.dto.GuideDto;
import com.example.course_work.entity.Guide;
import com.example.course_work.mapper.GuideMapper;
import com.example.course_work.repository.GuideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class GuideService {
    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;

    @Transactional(readOnly = true)
    public GuideDto getById(Long id){
        Guide guide =guideRepository.findById(id).orElseThrow();
        return guideMapper.toDto(guide);
    }
    public GuideDto createGuide(GuideCreationDto guide) {
        return guideMapper.toDto(guideRepository.save(guideMapper.toEntity(guide)));
    }
    @Transactional(readOnly = true)
    public List<GuideDto> getAllGuides() {
        return guideRepository.findAll().stream()
                .map(guideMapper::toDto)
                .toList();
    }
}
