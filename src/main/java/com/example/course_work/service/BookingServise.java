package com.example.course_work.service;

import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Client;
import com.example.course_work.mapper.BookingMapper;
import com.example.course_work.repository.AccommodationRepository;
import com.example.course_work.repository.BookingRepository;
import com.example.course_work.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BookingServise {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public BookingDto getById(Long id){
        Booking booking =bookingRepository.findById(id).orElseThrow();
        return bookingMapper.toDto(booking);
    }
    public BookingDto createBooking(BookingCreationDto bookingCreationDto) {
        Accommodation accommodation = accommodationRepository.findById(bookingCreationDto.accommodationId())
                .orElseThrow(() -> new IllegalArgumentException("Accommodation not found with ID: " + bookingCreationDto.accommodationId()));
        Client client = clientRepository.findById(bookingCreationDto.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + bookingCreationDto.clientId()));
        Booking booking = bookingMapper.toEntity(bookingCreationDto);
        booking.setAccommodation(accommodation);
        booking.setClient(client);
        booking.calculateTotalPrice();
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByClientId(Long clientId) {
        List<Booking> bookings = bookingRepository.findByClientId(clientId);
        return bookings.stream()
                .map(bookingMapper::toDto)
                .toList();
    }
}
