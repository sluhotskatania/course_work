package com.example.course_work.service;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Booking;
import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.exception.AccommodationNotFound;
import com.example.course_work.exception.BookingNotFound;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@AllArgsConstructor
@Transactional
public class AccommodationService {
    private static final Logger logger = LoggerFactory.getLogger(AccommodationService.class);

    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Transactional(readOnly = true)
    public AccommodationDto getById(Long id) {
        logger.info("Fetching accommodation by ID: {}", id);
        try {
            Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() -> new AccommodationNotFound("Accommodation not found"));
            logger.info("Accommodation with ID {} fetched successfully", id);
            return accommodationMapper.toDto(accommodation);
        } catch (AccommodationNotFound e) {
            logger.warn("Accommodation with ID {} not found", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching accommodation by ID: {}", id, e);
            throw e;
        }
    }
    public AccommodationDto createAccommodation(AccommodationCreationDto accommodation) {
        logger.info("Creating accommodation: {}", accommodation);
        try {
            Accommodation savedAccommodation = accommodationRepository.save(accommodationMapper.toEntity(accommodation));
            logger.info("Accommodation created successfully with ID: {}", savedAccommodation.getId());
            return accommodationMapper.toDto(savedAccommodation);
        } catch (Exception e) {
            logger.error("Error while creating accommodation: {}", accommodation, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<AccommodationDto> getAllAccommodations(Pageable pageable) {
        logger.info("Fetching accommodations with pagination and sorting");
        try {
            Page<AccommodationDto> accommodations = accommodationRepository.findAll(pageable)
                    .map(accommodationMapper::toDto);
            logger.info("Successfully fetched {} accommodations on page {}", accommodations.getNumberOfElements(), pageable.getPageNumber());
            return accommodations;
        } catch (Exception e) {
            logger.error("Error while fetching paginated accommodations", e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Page<AccommodationDto> getSortedAccommodations(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted accommodations by {} in {} order", sortBy, order);
        try {
            Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
            Page<Accommodation> accommodations = accommodationRepository.findAll(sortedPageable);
            logger.info("Successfully fetched sorted accommodations");
            return accommodations.map(accommodation -> new AccommodationDto(accommodation.getId(), accommodation.getCreated(), accommodation.getName(),
                    accommodation.getLocation(), accommodation.getType(), accommodation.getPricePerNight(), accommodation.getAvailability()));
        } catch (Exception e) {
            logger.error("Error while fetching sorted accommodations by {} in {} order", sortBy, order, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<AccommodationDto> getFilteredAccommodations(String name, String location, TypeAccommodationEnum type,
                                                            Double minPrice, Double maxPrice, Integer minAvailability,
                                                            Integer maxAvailability, Pageable pageable) {
        logger.info("Fetching filtered accommodations with name: {}, location: {}, type: {}, price range: {}-{}, availability range: {}-{}",
                name, location, type, minPrice, maxPrice, minAvailability, maxAvailability);
        try {
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
            logger.info("Successfully fetched filtered accommodations");
            return accommodations.map(accommodation -> new AccommodationDto(accommodation.getId(), accommodation.getCreated(), accommodation.getName(),
                    accommodation.getLocation(), accommodation.getType(), accommodation.getPricePerNight(), accommodation.getAvailability()));
        } catch (Exception e) {
            logger.error("Error while fetching filtered accommodations", e);
            throw e;
        }
    }

    public AccommodationDto updateAccommodation(Long id, AccommodationDto accommodationDto) {
        logger.info("Updating accommodation with ID: {}", id);
        try {
            Accommodation accommodation = accommodationRepository.findById(id)
                    .orElseThrow(() -> new AccommodationNotFound("Accommodation not found"));
            accommodationMapper.partialUpdate(accommodationDto, accommodation);
            Accommodation updatedAccommodation = accommodationRepository.save(accommodation);
            logger.info("Accommodation with ID: {} updated successfully", id);
            return accommodationMapper.toDto(updatedAccommodation);
        } catch (AccommodationNotFound e) {
            logger.warn("Accommodation with ID: {} not found", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating accommodation with ID: {}", id, e);
            throw e;
        }
    }
    @Transactional
    public void deleteAccommodation(Long id) {
        logger.info("Attempting to mark Accommodation with ID: {} as deleted", id);
        try {
            Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() -> {
                logger.warn("Accommodation with ID: {} not found", id);
                return new AccommodationNotFound("Accommodation with ID " + id + " not found.");
            });
            accommodation.setDeleted(true);
            accommodationRepository.save(accommodation);
            logger.info("Accommodation with ID: {} marked as deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while marking Accommodation with ID: {} as deleted", id, e);
            throw e;
        }
    }

}
