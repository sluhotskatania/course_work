package com.example.course_work.controller;

import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Client;
import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import com.example.course_work.service.BookingServise;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Get booking by ID",
            description = "Fetches booking details based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched booking",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDto.class))),
                    @ApiResponse(responseCode = "404", description = "Booking not found")
            }
    )
    @GetMapping("{id}")
    @Cacheable(value = "bookings", key = "#id")
    public ResponseEntity<BookingDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingServise.getById(id));
    }

    @Operation(
            summary = "Create a new booking",
            description = "Creates a new booking based on the provided data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Booking created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    @CacheEvict(value = "bookings", allEntries = true)
    public ResponseEntity<BookingDto> addBooking(@Valid @RequestBody BookingCreationDto bookingCreationDto) {
        return new ResponseEntity<>(bookingServise.createBooking(bookingCreationDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get paginated bookings",
            description = "Fetches paginated list of bookings with optional sorting",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched bookings",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    @Cacheable(value = "bookings")
    public ResponseEntity<Page<BookingDto>> getBookings(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String[] sort) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<BookingDto> bookings = bookingServise.getAllBookings(pageable);
        return ResponseEntity.ok(bookings);
    }

    @Operation(
            summary = "Get sorted bookings",
            description = "Fetches bookings sorted by the specified field and order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched sorted bookings",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/sorted")
    public Page<BookingDto> getSortedBookings(
            @RequestParam String sortBy,
            @RequestParam String order,
            Pageable pageable) {
        return bookingServise.getSortedBookings(sortBy, order, pageable);
    }

    @Operation(
            summary = "Get filtered bookings",
            description = "Fetches bookings based on the provided filter criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched filtered bookings",
                            content = @Content(mediaType = "application/json"))
            }
    )
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

    @Operation(
            summary = "Update an existing booking",
            description = "Updates the booking details based on the provided ID and data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated booking",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDto.class))),
                    @ApiResponse(responseCode = "404", description = "Booking not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "bookings", allEntries = true)
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingDto bookingDto) {
        BookingDto updatedBooking = bookingServise.updateBooking(id, bookingDto);
        return ResponseEntity.ok(updatedBooking);
    }
}
