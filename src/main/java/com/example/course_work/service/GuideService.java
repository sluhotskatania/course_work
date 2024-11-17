package com.example.course_work.service;

import com.example.course_work.dto.GuideCreationDto;
import com.example.course_work.dto.GuideDto;
import com.example.course_work.dto.GuideSortDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Guide;
import com.example.course_work.entity.Tour;
import com.example.course_work.enums.LanguagesEnum;
import com.example.course_work.mapper.GuideMapper;
import com.example.course_work.repository.GuideRepository;
import com.example.course_work.repository.TourRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class GuideService {
    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;
    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public GuideDto getById(Long id){
        Guide guide =guideRepository.findById(id).orElseThrow();
        return guideMapper.toDto(guide);
    }
    public GuideDto createGuide(GuideCreationDto guideCreationDto) {
        Tour tour = tourRepository.findById(guideCreationDto.tourId())
                .orElseThrow(() -> new IllegalArgumentException("Accommodation not found with ID: " + guideCreationDto.tourId()));
        Guide guide = guideMapper.toEntity(guideCreationDto);
        guide.setTour(tour);
        Guide savedGuide = guideRepository.save(guide);
        return guideMapper.toDto(savedGuide);
    }
    @Transactional(readOnly = true)
    public List<GuideSortDto> getAllGuides() {
        return guideRepository.findAll().stream()
                .map(guideMapper::toGdSortDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<GuideSortDto> getAllGuidesSortedByRatingAsc() {
        return guideRepository.findAll(Sort.by(Sort.Order.asc("rating")))
                .stream()
                .map(guideMapper::toGdSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GuideSortDto> getAllGuidesSortedByRatingDesc() {
        return guideRepository.findAll(Sort.by(Sort.Order.desc("rating")))
                .stream()
                .map(guideMapper::toGdSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GuideSortDto> getGuidesByLanguages(LanguagesEnum languages) {
        List<Guide> guides = guideRepository.findByLanguages(languages);
        return guides.stream()
                .map(guideMapper::toGdSortDto)
                .collect(Collectors.toList());
    }


}
