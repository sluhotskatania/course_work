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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<TourDto> getToursByDestination(String destination) {
        return tourRepository.findByDestinationContainingIgnoreCase(destination)
                .stream()
                .map(tourMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<TourDto> getToursByType(TypeEnum type) {
        return tourRepository.findByType(type)  // Використовуємо тип enum у репозиторії
                .stream()
                .map(tourMapper::toDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<TourSortDto> getAllToursSortedByDateAsc() {
        return tourRepository.findAllByOrderByDepartureDateAsc()
                .stream()
                .map(tourMapper::toSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TourSortDto> getAllToursSortedByDateDesc() {
        return tourRepository.findAllByOrderByDepartureDateDesc()
                .stream()
                .map(tourMapper::toSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TourSortDto> getAllToursSortedByPriceAsc() {
        return tourRepository.findAll(Sort.by(Sort.Order.asc("price")))
                .stream()
                .map(tourMapper::toSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TourSortDto> getAllToursSortedByPriceDesc() {
        return tourRepository.findAll(Sort.by(Sort.Order.desc("price")))
                .stream()
                .map(tourMapper::toSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TourSortDto> getAllToursSortedByDurationAsc() {
        return tourRepository.findAll(Sort.by(Sort.Order.asc("duration")))
                .stream()
                .map(tourMapper::toSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TourSortDto> getAllToursSortedByDurationDesc() {
        return tourRepository.findAll(Sort.by(Sort.Order.desc("duration")))
                .stream()
                .map(tourMapper::toSortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TourSortDto> getAllToursSorted(String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Order.asc(sortBy))
                : Sort.by(Sort.Order.desc(sortBy));

        return tourRepository.findAll(sort)
                .stream()
                .map(tourMapper::toSortDto)
                .toList();
    }

}
