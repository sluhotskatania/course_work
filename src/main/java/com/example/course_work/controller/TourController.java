package com.example.course_work.controller;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.dto.TourSortDto;
import com.example.course_work.enums.TypeEnum;
import com.example.course_work.service.TourService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/filter/destination")
    public ResponseEntity<List<TourDto>> getToursByDestination(@RequestParam String destination) {
        return ResponseEntity.ok(tourService.getToursByDestination(destination));
    }
    @GetMapping("/filter/type")
    public ResponseEntity<List<TourDto>> getToursByType(@RequestParam String type) {
        try {
            TypeEnum typeEnum = TypeEnum.valueOf(type.toUpperCase());
            return ResponseEntity.ok(tourService.getToursByType(typeEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/sorted/asc")
    public ResponseEntity<List<TourSortDto>> getToursSortedByDateAsc() {
        return ResponseEntity.ok(tourService.getAllToursSortedByDateAsc());
    }

    @GetMapping("/sorted/desc")
    public ResponseEntity<List<TourSortDto>> getToursSortedByDateDesc() {
        return ResponseEntity.ok(tourService.getAllToursSortedByDateDesc());
    }
    @GetMapping("/sorted/priceAsc")
    public ResponseEntity<List<TourSortDto>> getToursSortedByPriceAsc() {
        return ResponseEntity.ok(tourService.getAllToursSortedByPriceAsc());
    }

    // Сортування за ціною (спаданням)
    @GetMapping("/sorted/priceDesc")
    public ResponseEntity<List<TourSortDto>> getToursSortedByPriceDesc() {
        return ResponseEntity.ok(tourService.getAllToursSortedByPriceDesc());
    }

    // Сортування за тривалістю (зростанням)
    @GetMapping("/sorted/durationAsc")
    public ResponseEntity<List<TourSortDto>> getToursSortedByDurationAsc() {
        return ResponseEntity.ok(tourService.getAllToursSortedByDurationAsc());
    }

    // Сортування за тривалістю (спаданням)
    @GetMapping("/sorted/durationDesc")
    public ResponseEntity<List<TourSortDto>> getToursSortedByDurationDesc() {
        return ResponseEntity.ok(tourService.getAllToursSortedByDurationDesc());
    }

    // Гнучке сортування (по будь-якому полю)
    @GetMapping("/sorted")
    public ResponseEntity<List<TourSortDto>> getAllToursSorted(
            @RequestParam String sortBy,
            @RequestParam String order) {

        return ResponseEntity.ok(tourService.getAllToursSorted(sortBy, order));
    }
}
