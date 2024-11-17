package com.example.course_work.controller;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.enums.TypeAccommodationEnum;
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
    @GetMapping("/filter/type")
    public ResponseEntity<List<AccommodationDto>> getTourAccommodationsByType(@RequestParam String type) {
        try {
            TypeAccommodationEnum typeAccommodationEnum = TypeAccommodationEnum.valueOf(type.toUpperCase());
            return ResponseEntity.ok(accommodationService.getTourAccommodationsByType(typeAccommodationEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/filter/location")
    public ResponseEntity<List<AccommodationDto>> getAccommodationsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(accommodationService.getAccommodationsByLocation(location));
    }
}
