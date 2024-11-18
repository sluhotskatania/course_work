package com.example.course_work.service;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.enums.TypeEnum;
import com.example.course_work.mapper.AccommodationMapper;
import com.example.course_work.repository.AccommodationRepository;
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
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Transactional(readOnly = true)
    public AccommodationDto getById(Long id){
        Accommodation accommodation =accommodationRepository.findById(id).orElseThrow();
        return accommodationMapper.toDto(accommodation);
    }
    public AccommodationDto createAccommodation(AccommodationCreationDto accommodation) {
        return accommodationMapper.toDto(accommodationRepository.save(accommodationMapper.toEntity(accommodation)));
    }
    @Transactional(readOnly = true)
    public List<AccommodationDto> getAllAccommodations() {
        return accommodationRepository.findAll().stream()
                .map(accommodationMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public Page<AccommodationDto> getSortedAccommodations(String sortBy, String order, Pageable pageable) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        Page<Accommodation> accommodations = accommodationRepository.findAll(sortedPageable);
        return accommodations.map(accommodation -> new AccommodationDto(
                accommodation.getId(),
                accommodation.getCreated(),
                accommodation.getName(),
                accommodation.getLocation(),
                accommodation.getType(),
                accommodation.getPricePerNight(),
                accommodation.getAvailability()
        ));
    }
    @Transactional(readOnly = true)
    public Page<AccommodationDto> getFilteredAccommodations(String name, String location, TypeAccommodationEnum type,
                                                            Double minPrice, Double maxPrice, Integer minAvailability,
                                                            Integer maxAvailability, Pageable pageable) {
        Specification<Accommodation> specification = Specification.where(null);
        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (location != null && !location.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
        }
        if (type != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }
        if (minPrice != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("pricePerNight"), minPrice));
        }
        if (maxPrice != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("pricePerNight"), maxPrice));
        }
        if (minAvailability != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("availability"), minAvailability));
        }
        if (maxAvailability != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("availability"), maxAvailability));
        }
        Page<Accommodation> accommodations = accommodationRepository.findAll(specification, pageable);
        return accommodations.map(accommodation -> new AccommodationDto(
                accommodation.getId(),
                accommodation.getCreated(),
                accommodation.getName(),
                accommodation.getLocation(),
                accommodation.getType(),
                accommodation.getPricePerNight(),
                accommodation.getAvailability()
        ));
    }




}
