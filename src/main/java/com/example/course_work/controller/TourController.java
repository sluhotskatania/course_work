package com.example.course_work.controller;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
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
}
