package com.example.course_work.service;

import com.example.course_work.dto.*;
import com.example.course_work.entity.Guide;
import com.example.course_work.entity.Tour;
import com.example.course_work.enums.LanguagesEnum;
import com.example.course_work.exception.GuideNotFound;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@AllArgsConstructor
@Transactional
public class GuideService {
    private static final Logger logger = LoggerFactory.getLogger(GuideService.class);
    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;
    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public GuideDto getById(Long id) {
        logger.info("Fetching guide by ID: {}", id);
        try {
            Guide guide = guideRepository.findById(id).orElseThrow(() -> new GuideNotFound("Guide not found"));
            logger.info("Guide with ID {} fetched successfully", id);
            return guideMapper.toDto(guide);
        } catch (GuideNotFound e) {
            logger.warn("Guide with ID {} not found", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching guide by ID: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public GuideDto createGuide(GuideCreationDto guideCreationDto) {
        logger.info("Creating guide for tour ID: {}", guideCreationDto.tourId());
        try {
            Tour tour = tourRepository.findById(guideCreationDto.tourId())
                    .orElseThrow(() -> new GuideNotFound("Guide not found with ID: " + guideCreationDto.tourId()));

            Guide guide = guideMapper.toEntity(guideCreationDto);
            guide.setTour(tour);

            Guide savedGuide = guideRepository.save(guide);
            logger.info("Guide created successfully with ID: {}", savedGuide.getId());
            return guideMapper.toDto(savedGuide);
        } catch (GuideNotFound e) {
            logger.warn("Guide with ID {} not found during guide creation", guideCreationDto.tourId(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while creating guide for tour ID: {}", guideCreationDto.tourId(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<GuideSortDto> getAllGuides(Pageable pageable) {
        logger.info("Fetching guides with pagination and sorting");
        try {
            Page<GuideSortDto> guides = guideRepository.findAll(pageable)
                    .map(guideMapper::toGdSortDto);
            logger.info("Fetched {} guides on page {}", guides.getNumberOfElements(), pageable.getPageNumber());
            return guides;
        } catch (Exception e) {
            logger.error("Error while fetching paginated and sorted guides", e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Page<GuideDto> getSortedGuides(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted guides by {} in {} order", sortBy, order);
        try {
            Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
            Page<Guide> guides = guideRepository.findAll(sortedPageable);

            logger.info("Fetched {} guides", guides.getTotalElements());
            return guides.map(guideMapper::toDto);
        } catch (Exception e) {
            logger.error("Error fetching sorted guides", e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Page<GuideDto> getFilteredGuides(String name, String surname, String email, String phone, LanguagesEnum language,
                                            Integer minExperience, Integer maxExperience, Double minRating, Double maxRating, Pageable pageable) {
        logger.info("Fetching filtered guides with criteria: name={}, surname={}, email={}, phone={}, language={}, " +
                "minExperience={}, maxExperience={}, minRating={}, maxRating={}", name, surname, email, phone, language, minExperience, maxExperience, minRating, maxRating);
        try {
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
            logger.info("Fetched {} guides matching the filters", guides.getTotalElements());
            return guides.map(guideMapper::toDto);
        } catch (Exception e) {
            logger.error("Error fetching filtered guides", e);
            throw e;
        }
    }

    @Transactional
    public GuideDto updateGuide(Long id, GuideDto guideDto) {
        logger.info("Updating guide with ID: {}", id);
        try {
            Guide guide = guideRepository.findById(id)
                    .orElseThrow(() -> new GuideNotFound("Guide not found with ID: " + id));
            guideMapper.partialUpdate(guideDto, guide);
            Guide updatedGuide = guideRepository.save(guide);

            logger.info("Guide with ID {} updated successfully", id);
            return guideMapper.toDto(updatedGuide);
        } catch (GuideNotFound e) {
            logger.warn("Guide with ID {} not found for update", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating guide with ID: {}", id, e);
            throw e;
        }
    }

}
