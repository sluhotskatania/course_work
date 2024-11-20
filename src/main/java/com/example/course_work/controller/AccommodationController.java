package com.example.course_work.controller;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/accommodations")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @GetMapping("{id}")
    @Cacheable(value = "accommodations", key = "#id")
    public ResponseEntity<AccommodationDto> getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(accommodationService.getById(id));
    }
    @PostMapping
    @CacheEvict(value = "accommodations", allEntries = true)
    public ResponseEntity<AccommodationDto> addAccommodation(@Valid @RequestBody AccommodationCreationDto accommodationCreationDto) {
        return new ResponseEntity<>(accommodationService.createAccommodation(accommodationCreationDto), HttpStatus.CREATED);
    }
    @GetMapping
    @Cacheable(value = "accommodations")
    public ResponseEntity<List<AccommodationDto>> getAllAccommodations() {
        return ResponseEntity.ok(accommodationService.getAllAccommodations());
    }
    @GetMapping("/sorted")
    public ResponseEntity<Page<AccommodationDto>> getSortedAccommodations(
            @RequestParam String sortBy,
            @RequestParam String order,
            Pageable pageable) {

        Page<AccommodationDto> accommodations = accommodationService.getSortedAccommodations(sortBy, order, pageable);
        return ResponseEntity.ok(accommodations);
    }
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
    @PutMapping("/{id}")
    @CacheEvict(value = "accommodations", allEntries = true)
    public ResponseEntity<AccommodationDto> updateAccommodation(@PathVariable Long id, @RequestBody @Valid AccommodationDto accommodationDto) {
        AccommodationDto updateAccommodation = accommodationService.updateAccommodation(id, accommodationDto);
        return ResponseEntity.ok(updateAccommodation);
    }
}
