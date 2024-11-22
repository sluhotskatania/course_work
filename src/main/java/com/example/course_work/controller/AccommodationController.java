package com.example.course_work.controller;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.service.AccommodationService;
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

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @Operation(
            summary = "Get accommodation by ID",
            description = "Fetches accommodation details based on provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched accommodation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AccommodationDto.class))),
                    @ApiResponse(responseCode = "404", description = "Accommodation not found")
            }
    )
    @GetMapping("{id}")
    @Cacheable(value = "accommodations", key = "#id")
    public ResponseEntity<AccommodationDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accommodationService.getById(id));
    }

    @Operation(
            summary = "Add a new accommodation",
            description = "Creates a new accommodation record",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Accommodation created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AccommodationDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    @CacheEvict(value = "accommodations", allEntries = true)
    public ResponseEntity<AccommodationDto> addAccommodation(@Valid @RequestBody AccommodationCreationDto accommodationCreationDto) {
        return new ResponseEntity<>(accommodationService.createAccommodation(accommodationCreationDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all accommodations with pagination",
            description = "Fetches all accommodations with support for pagination and sorting",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched accommodations",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping
    @Cacheable(value = "accommodations")
    public ResponseEntity<Page<AccommodationDto>> getAccommodations(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String[] sort) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<AccommodationDto> accommodations = accommodationService.getAllAccommodations(pageable);
        return ResponseEntity.ok(accommodations);
    }

    @Operation(
            summary = "Get sorted accommodations",
            description = "Fetches accommodations sorted by a specified field and order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched sorted accommodations",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<Page<AccommodationDto>> getSortedAccommodations(
            @RequestParam String sortBy,
            @RequestParam String order,
            Pageable pageable) {

        Page<AccommodationDto> accommodations = accommodationService.getSortedAccommodations(sortBy, order, pageable);
        return ResponseEntity.ok(accommodations);
    }

    @Operation(
            summary = "Get filtered accommodations",
            description = "Fetches accommodations filtered by various criteria such as name, location, type, price range, and availability",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched filtered accommodations",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<Page<AccommodationDto>> getFilteredAccommodations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) TypeAccommodationEnum type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minAvailability,
            @RequestParam(required = false) Integer maxAvailability,
            Pageable pageable) {

        Page<AccommodationDto> accommodations = accommodationService.getFilteredAccommodations(
                name, location, type, minPrice, maxPrice, minAvailability, maxAvailability, pageable);

        return ResponseEntity.ok(accommodations);
    }

    @Operation(
            summary = "Update accommodation by ID",
            description = "Updates an existing accommodation based on the provided ID and new data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Accommodation updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AccommodationDto.class))),
                    @ApiResponse(responseCode = "404", description = "Accommodation not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "accommodations", allEntries = true)
    public ResponseEntity<AccommodationDto> updateAccommodation(@PathVariable Long id, @RequestBody @Valid AccommodationDto accommodationDto) {
        AccommodationDto updateAccommodation = accommodationService.updateAccommodation(id, accommodationDto);
        return ResponseEntity.ok(updateAccommodation);
    }
}

