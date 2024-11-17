package com.example.course_work.controller;

import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.service.BookingServise;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/booking")
public class BookingController {
    private final BookingServise bookingServise;

    @GetMapping("{id}")
    public ResponseEntity<BookingDto> getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(bookingServise.getById(id));
    }
    @PostMapping
    public ResponseEntity<BookingDto> addBooking(@Valid @RequestBody BookingCreationDto bookingCreationDto) {
        return new ResponseEntity<>(bookingServise.createBooking(bookingCreationDto), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingServise.getAllBookings());
    }
    @GetMapping("/client/{clientId}")
    public List<BookingDto> getBookingsByClientId(@PathVariable Long clientId) {
        return bookingServise.getBookingsByClientId(clientId); // Виправлено виклик сервісу
    }
}
