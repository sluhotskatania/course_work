package com.example.course_work.service;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Tour;
import com.example.course_work.enums.TypeEnum;
import com.example.course_work.exception.TourNotFound;
import com.example.course_work.mapper.TourMapper;
import com.example.course_work.repository.AccommodationRepository;
import com.example.course_work.repository.BookingRepository;
import com.example.course_work.repository.TourRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
@Transactional
public class TourService {
    private static final Logger logger = LoggerFactory.getLogger(TourService.class);
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public TourDto getById(Long id) {
        logger.info("Fetching tour by ID: {}", id);
        try {
            Tour tour = tourRepository.findById(id)
                    .orElseThrow(() -> new TourNotFound("Tour not found"));
            logger.info("Tour with ID {} fetched successfully", id);
            return tourMapper.toDto(tour);
        } catch (TourNotFound e) {
            logger.warn("Tour with ID {} not found", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching tour by ID: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public TourDto createTour(TourCreationDto tourCreationDto) {
        logger.info("Creating a new tour with Booking ID: {} and Accommodation ID: {}",
                tourCreationDto.bookingId(), tourCreationDto.accommodationId());
        try {
            Booking booking = bookingRepository.findById(tourCreationDto.bookingId())
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + tourCreationDto.bookingId()));
            logger.debug("Booking with ID {} found", tourCreationDto.bookingId());

            Accommodation accommodation = accommodationRepository.findById(tourCreationDto.accommodationId())
                    .orElseThrow(() -> new IllegalArgumentException("Accommodation not found with ID: " + tourCreationDto.accommodationId()));
            logger.debug("Accommodation with ID {} found", tourCreationDto.accommodationId());

            Tour tour = tourMapper.toEntity(tourCreationDto);
            tour.setBooking(booking);
            tour.setAccommodation(accommodation);

            Tour savedTour = tourRepository.save(tour);
            logger.info("Tour created successfully with ID: {}", savedTour.getId());
            return tourMapper.toDto(savedTour);
        } catch (IllegalArgumentException e) {
            logger.warn("Error creating tour: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while creating tour", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<TourDto> getAllTours(Pageable pageable) {
        logger.info("Fetching tours with pagination and sorting");
        try {
            Page<TourDto> tours = tourRepository.findAll(pageable)
                    .map(tourMapper::toDto);
            logger.info("Successfully fetched {} tours on page {}", tours.getNumberOfElements(), pageable.getPageNumber());
            return tours;
        } catch (Exception e) {
            logger.error("Error while fetching paginated tours", e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Page<TourDto> getSortedTours(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted tours with sortBy: {} and order: {}", sortBy, order);
        try {
            Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
            Page<Tour> tours = tourRepository.findAll(sortedPageable);

            logger.info("Successfully fetched sorted tours");
            return tours.map(tour -> new TourDto(
                    tour.getId(),  tour.getName(), tour.getDestination(),
                    tour.getDuration(), tour.getPrice(), tour.getDepartureDate(),
                    tour.getReturnDate(), tour.getType(), tour.getMaxParticipants(),
                    tour.getAccommodation().getName(),
                    tour.getAccommodation().getLocation(), tour.getAccommodation().getType(),
                    tour.getBooking().getTotalPrice(), tour.getBooking().getNotes()
            ));
        } catch (Exception e) {
            logger.error("Error fetching sorted tours", e);
            throw e;
        }
    }
    @Transactional(readOnly = true)
    public Page<TourDto> getFilteredTours(String name, String destination, Integer duration, Double minPrice, Double maxPrice,
                                          Date departureDate, Date returnDate, TypeEnum type, Integer maxParticipants,
                                          Pageable pageable) {
        logger.info("Fetching filtered tours with criteria: name={}, destination={}, duration={}, minPrice={}, maxPrice={}, " +
                        "departureDate={}, returnDate={}, type={}, maxParticipants={}",
                name, destination, duration, minPrice, maxPrice, departureDate, returnDate, type, maxParticipants);
        try {
            Specification<Tour> specification = Specification.where(null);

            if (name != null && !name.isEmpty()) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (destination != null && !destination.isEmpty()) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("destination")), "%" + destination.toLowerCase() + "%"));
            }
            if (duration != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("duration"), duration));
            }
            if (minPrice != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (departureDate != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("departureDate"), departureDate));
            }
            if (returnDate != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("returnDate"), returnDate));
            }
            if (type != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("type"), type));
            }
            if (maxParticipants != null) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("maxParticipants"), maxParticipants));
            }

            Page<Tour> tours = tourRepository.findAll(specification, pageable);

            logger.info("Successfully fetched {} filtered tours", tours.getTotalElements());
            return tours.map(tour -> new TourDto(
                    tour.getId(),  tour.getName(), tour.getDestination(), tour.getDuration(),
                    tour.getPrice(), tour.getDepartureDate(), tour.getReturnDate(), tour.getType(),
                    tour.getMaxParticipants(),
                    tour.getAccommodation().getName(), tour.getAccommodation().getLocation(),
                    tour.getAccommodation().getType(), tour.getBooking().getTotalPrice(), tour.getBooking().getNotes()
            ));
        } catch (Exception e) {
            logger.error("Error fetching filtered tours", e);
            throw e;
        }
    }
    @Transactional
    public TourDto updateTour(Long id, TourDto tourDto) {
        logger.info("Updating tour with ID: {}", id);
        try {
            Tour tour = tourRepository.findById(id)
                    .orElseThrow(() -> new TourNotFound("Tour not found with ID: " + id));
            tourMapper.partialUpdate(tourDto, tour);
            Tour updatedTour = tourRepository.save(tour);
            logger.info("Tour with ID {} updated successfully", id);
            return tourMapper.toDto(updatedTour);
        } catch (TourNotFound e) {
            logger.warn("Tour not found for update with ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating tour with ID: {}", id, e);
            throw e;
        }
    }
    @Transactional
    public void deleteTour(Long id) {
        logger.info("Attempting to mark Tour with ID: {} as deleted", id);
        try {
            Tour tour = tourRepository.findById(id).orElseThrow(() -> {
                logger.warn("Tour with ID: {} not found", id);
                return new TourNotFound("Tour with ID " + id + " not found.");
            });
            tour.setDeleted(true);
            tourRepository.save(tour);
            logger.info("Tour with ID: {} marked as deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while marking Tour with ID: {} as deleted", id, e);
            throw e;
        }
    }



}
