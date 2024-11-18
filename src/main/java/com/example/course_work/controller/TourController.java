package com.example.course_work.controller;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.dto.TourSortDto;
import com.example.course_work.enums.TypeEnum;
import com.example.course_work.service.TourService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/tour")
public class TourController {
    public final TourService tourService;

    @GetMapping("{id}")
    public ResponseEntity<TourDto> getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(tourService.getById(id));
    }
    @PostMapping
    public ResponseEntity<TourDto> addTour(@Valid @RequestBody TourCreationDto tourCreationDto) {
        return new ResponseEntity<>(tourService.createTour(tourCreationDto), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }
    @GetMapping("/sorted")
    public ResponseEntity<Page<TourDto>> getSortedTours(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            Pageable pageable) {
        Page<TourDto> sortedTours = tourService.getSortedTours(sortBy, order, pageable);
        return ResponseEntity.ok(sortedTours);
    }
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
}
