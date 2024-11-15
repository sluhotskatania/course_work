package com.example.course_work.controller;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @GetMapping("{id}")
    public ResponseEntity<AccommodationDto> getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(accommodationService.getById(id));
    }
    @PostMapping
    public ResponseEntity<AccommodationDto> addAccommodation(@Valid @RequestBody AccommodationCreationDto accommodationCreationDto) {
        return new ResponseEntity<>(accommodationService.createAccommodation(accommodationCreationDto), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<AccommodationDto>> getAllAccommodations() {
        return ResponseEntity.ok(accommodationService.getAllAccommodations());
    }
}
