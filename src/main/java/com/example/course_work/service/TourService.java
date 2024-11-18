package com.example.course_work.service;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.dto.TourSortDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Tour;
import com.example.course_work.enums.TypeEnum;
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

@Service
@AllArgsConstructor
@Transactional
public class TourService {
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public TourDto getById(Long id) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        return tourMapper.toDto(tour);
    }

    public TourDto createTour(TourCreationDto tourCreationDto) {
        Booking booking = bookingRepository.findById(tourCreationDto.bookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + tourCreationDto.bookingId()));
        Tour tour = tourMapper.toEntity(tourCreationDto);
        tour.setBooking(booking);

        Accommodation accommodation = accommodationRepository.findById(tourCreationDto.accommodationId())
                .orElseThrow(() -> new IllegalArgumentException("Accommodation not found with ID: " + tourCreationDto.accommodationId()));
        tour.setAccommodation(accommodation);
        return tourMapper.toDto(tourRepository.save(tour));
    }

    @Transactional(readOnly = true)
    public List<TourDto> getAllTours() {
        return tourRepository.findAll().stream()
                .map(tourMapper::toDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public Page<TourDto> getSortedTours(String sortBy, String order, Pageable pageable) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        Page<Tour> tours = tourRepository.findAll(sortedPageable);

        return tours.map(tour -> new TourDto(tour.getId(), tour.getCreated(),tour.getName(), tour.getDestination(), tour.getDuration(),
                tour.getPrice(), tour.getDepartureDate(), tour.getReturnDate(), tour.getType(), tour.getMaxParticipants(), tour.getAccommodation().getId(),
                tour.getAccommodation().getName(), tour.getAccommodation().getLocation(), tour.getAccommodation().getType(), tour.getAccommodation().getPricePerNight(),
                tour.getBooking().getId(), tour.getBooking().getTotalPrice(), tour.getBooking().getNotes()
        ));
    }

    @Transactional(readOnly = true)
    public Page<TourDto> getFilteredTours(String name, String destination, Integer duration, Double minPrice, Double maxPrice,
                                          Date departureDate, Date returnDate, TypeEnum type, Integer maxParticipants,
                                          Pageable pageable) {
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

        return tours.map(tour -> new TourDto(tour.getId(), tour.getCreated(), tour.getName(), tour.getDestination(), tour.getDuration(), tour.getPrice(),
                tour.getDepartureDate(), tour.getReturnDate(), tour.getType(), tour.getMaxParticipants(), tour.getAccommodation().getId(),
                tour.getAccommodation().getName(), tour.getAccommodation().getLocation(), tour.getAccommodation().getType(), tour.getAccommodation().getPricePerNight(),
                tour.getBooking().getId(), tour.getBooking().getTotalPrice(), tour.getBooking().getNotes()
        ));
    }


}
