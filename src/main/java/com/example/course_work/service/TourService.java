package com.example.course_work.service;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Tour;
import com.example.course_work.mapper.TourMapper;
import com.example.course_work.repository.TourRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TourService {
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    @Transactional(readOnly = true)
    public TourDto getById(Long id){
        Tour tour =tourRepository.findById(id).orElseThrow();
        return tourMapper.toDto(tour);
    }
    public TourDto createTour(TourCreationDto tour) {
        return tourMapper.toDto(tourRepository.save(tourMapper.toEntity(tour)));
    }
    @Transactional(readOnly = true)
    public List<TourDto> getAllTours() {
        return tourRepository.findAll().stream()
                .map(tourMapper::toDto)
                .toList();
    }
}
