package com.example.course_work.controller;

import com.example.course_work.dto.*;
import com.example.course_work.enums.LanguagesEnum;
import com.example.course_work.service.GuideService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/guides")
public class GuideController {
    private final GuideService guideService;

    @GetMapping("{id}")
    public ResponseEntity<GuideDto> getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(guideService.getById(id));
    }
    @PostMapping
    public ResponseEntity<GuideDto> addGuide(@Valid @RequestBody GuideCreationDto guideCreationDto) {
        return new ResponseEntity<>(guideService.createGuide(guideCreationDto), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<GuideSortDto>> getAllGuides() {
        return ResponseEntity.ok(guideService.getAllGuides());
    }
    @GetMapping("/sorted")
    public ResponseEntity<Page<GuideDto>> getSortedGuides(
                @RequestParam(defaultValue = "name") String sortBy,
                @RequestParam(defaultValue = "asc") String order,
                Pageable pageable) {
            Page<GuideDto> sortedGuides = guideService.getSortedGuides(sortBy, order, pageable);
            return ResponseEntity.ok(sortedGuides);
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<GuideDto>> getFilteredGuides(
                @RequestParam(required = false) String name,
                @RequestParam(required = false) String surname,
                @RequestParam(required = false) String email,
                @RequestParam(required = false) String phone,
                @RequestParam(required = false) LanguagesEnum language,
                @RequestParam(required = false) Integer minExperience,
                @RequestParam(required = false) Integer maxExperience,
                @RequestParam(required = false) Double minRating,
                @RequestParam(required = false) Double maxRating,
                Pageable pageable) {
            Page<GuideDto> filteredGuides = guideService.getFilteredGuides(
                    name, surname, email, phone, language, minExperience, maxExperience, minRating, maxRating, pageable);
            return ResponseEntity.ok(filteredGuides);
    }
    @PutMapping("/{id}")
    public ResponseEntity<GuideDto> updateGuide(@PathVariable Long id, @RequestBody @Valid GuideDto guideDto) {
        GuideDto updateGuide = guideService.updateGuide(id, guideDto);
        return ResponseEntity.ok(updateGuide);
    }
}

