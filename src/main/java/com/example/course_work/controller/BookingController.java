package com.example.course_work.controller;

import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.dto.ClientDto;
import com.example.course_work.entity.Client;
import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import com.example.course_work.service.BookingServise;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/bookings")
public class BookingController {
    private final BookingServise bookingServise;

    @GetMapping("{id}")
    @Cacheable(value = "bookings", key = "#id")
    public ResponseEntity<BookingDto> getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(bookingServise.getById(id));
    }
    @PostMapping
    @CacheEvict(value = "bookings", allEntries = true)
    public ResponseEntity<BookingDto> addBooking(@Valid @RequestBody BookingCreationDto bookingCreationDto) {
        return new ResponseEntity<>(bookingServise.createBooking(bookingCreationDto), HttpStatus.CREATED);
    }
    @GetMapping
    @Cacheable(value = "bookings")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingServise.getAllBookings());
    }
    @GetMapping("/sorted")
    public Page<BookingDto> getSortedBookings(
            @RequestParam String sortBy,
            @RequestParam String order,
            Pageable pageable) {
        return bookingServise.getSortedBookings(sortBy, order, pageable);
    }
    @GetMapping("/filtered")
    public Page<BookingDto> getFilteredBookings(
            @RequestParam(required = false) Client client,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) BookingStatusEnum status,
            @RequestParam(required = false) PaymentStatusEnum paymentStatus,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        return bookingServise.getFilteredBookings(client, startDate, endDate, status, paymentStatus, minPrice, maxPrice, pageable);
    }
    @PutMapping("/{id}")
    @CacheEvict(value = "bookings", allEntries = true)
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingDto bookingDto) {
        BookingDto updateBooking = bookingServise.updateBooking(id, bookingDto);
        return ResponseEntity.ok(updateBooking);
    }
}
