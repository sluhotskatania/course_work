package com.example.course_work.controller;

import com.example.course_work.dto.GuideDto;
import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.dto.TourSortDto;
import com.example.course_work.enums.TypeEnum;
import com.example.course_work.service.TourService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/tours")
public class TourController {
    public final TourService tourService;

    @Operation(
            summary = "Get tour by ID",
            description = "Fetches a tour's details based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched tour",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TourDto.class))),
                    @ApiResponse(responseCode = "404", description = "Tour not found")
            }
    )
    @GetMapping("{id}")
    @Cacheable(value = "tours", key = "#id")
    public ResponseEntity<TourDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tourService.getById(id));
    }

    @Operation(
            summary = "Add a new tour",
            description = "Creates a new tour from the provided TourCreationDto data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tour successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TourDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    @CacheEvict(value = "tours", allEntries = true)
    public ResponseEntity<TourDto> addTour(@Valid @RequestBody TourCreationDto tourCreationDto) {
        return new ResponseEntity<>(tourService.createTour(tourCreationDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all tours with pagination",
            description = "Fetches a paginated list of all tours, sorted by the specified parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched tours",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TourDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters")
            }
    )
    @GetMapping
    @Cacheable(value = "tours")
    public ResponseEntity<Page<TourDto>> getTours(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<TourDto> tours = tourService.getAllTours(pageable);
        return ResponseEntity.ok(tours);
    }

    @Operation(
            summary = "Get tours sorted by a specific field",
            description = "Fetches a paginated and sorted list of tours based on the given sorting parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched sorted tours",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TourDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid sorting parameters")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<Page<TourDto>> getSortedTours(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            Pageable pageable) {
        Page<TourDto> sortedTours = tourService.getSortedTours(sortBy, order, pageable);
        return ResponseEntity.ok(sortedTours);
    }

    @Operation(
            summary = "Get filtered tours",
            description = "Fetches a paginated list of tours filtered by various parameters (e.g., name, destination, price range)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched filtered tours",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TourDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid filter parameters")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<Page<TourDto>> getFilteredTours(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date returnDate,
            @RequestParam(required = false) TypeEnum type,
            @RequestParam(required = false) Integer maxParticipants,
            Pageable pageable) {
        Page<TourDto> filteredTours = tourService.getFilteredTours(
                name, destination, duration, minPrice, maxPrice, departureDate, returnDate, type, maxParticipants, pageable
        );
        return ResponseEntity.ok(filteredTours);
    }

    @Operation(
            summary = "Update tour details",
            description = "Updates an existing tour based on the provided tour ID and updated tour data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated tour",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TourDto.class))),
                    @ApiResponse(responseCode = "404", description = "Tour not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "tours", allEntries = true)
    public ResponseEntity<TourDto> updateTour(@PathVariable Long id, @RequestBody @Valid TourDto tourDto) {
        TourDto updateTour = tourService.updateTour(id, tourDto);
        return ResponseEntity.ok(updateTour);
    }
}

