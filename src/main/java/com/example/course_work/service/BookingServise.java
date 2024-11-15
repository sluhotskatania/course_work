package com.example.course_work.service;


import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Booking;
import com.example.course_work.mapper.BookingMapper;
import com.example.course_work.repository.BookingRepository;
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

    @Transactional(readOnly = true)
    public BookingDto getById(Long id){
        Booking booking =bookingRepository.findById(id).orElseThrow();
        return bookingMapper.toDto(booking);
    }
    public BookingDto createBooking(BookingCreationDto booking) {
        return bookingMapper.toDto(bookingRepository.save(bookingMapper.toEntity(booking)));
    }
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }
}
