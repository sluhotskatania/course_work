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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<GuideDto> getSortedGuides(String sortBy, String order, Pageable pageable) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        Page<Guide> guides = guideRepository.findAll(sortedPageable);
        return guides.map(guide -> new GuideDto(
                guide.getId(),
                guide.getCreated(),
                guide.getName(),
                guide.getSurname(),
                guide.getEmail(),
                guide.getPhone(),
                guide.getLanguages(),
                guide.getExperience(),
                guide.getRating(),
                guide.getTour().getId(),
                guide.getTour().getName(),
                guide.getTour().getDestination(),
                guide.getTour().getDuration(),
                guide.getTour().getDepartureDate(),
                guide.getTour().getReturnDate(),
                guide.getTour().getType()
        ));
    }

    @Transactional(readOnly = true)
    public Page<GuideDto> getFilteredGuides(String name, String surname, String email, String phone, LanguagesEnum language,
                                            Integer minExperience, Integer maxExperience, Double minRating, Double maxRating, Pageable pageable) {
        Specification<Guide> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (surname != null && !surname.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%"));
        }
        if (email != null && !email.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
        }
        if (language != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("languages"), language));
        }
        if (minExperience != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience));
        }
        if (maxExperience != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience));
        }
        if (minRating != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
        }
        if (maxRating != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));
        }

        Page<Guide> guides = guideRepository.findAll(specification, pageable);
        return guides.map(guide -> new GuideDto(
                guide.getId(),
                guide.getCreated(),
                guide.getName(),
                guide.getSurname(),
                guide.getEmail(),
                guide.getPhone(),
                guide.getLanguages(),
                guide.getExperience(),
                guide.getRating(),
                guide.getTour().getId(),
                guide.getTour().getName(),
                guide.getTour().getDestination(),
                guide.getTour().getDuration(),
                guide.getTour().getDepartureDate(),
                guide.getTour().getReturnDate(),
                guide.getTour().getType()
        ));
    }

}
