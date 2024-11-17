package com.example.course_work.controller;

import com.example.course_work.dto.GuideCreationDto;
import com.example.course_work.dto.GuideDto;
import com.example.course_work.dto.GuideSortDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.enums.LanguagesEnum;
import com.example.course_work.service.GuideService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/guide")
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
    @GetMapping("/filter/languages")
    public ResponseEntity<List<GuideSortDto>> getGuidesByLanguages(@RequestParam String languages) {
        try {
            LanguagesEnum languageEnum = LanguagesEnum.valueOf(languages.toUpperCase());
            return ResponseEntity.ok(guideService.getGuidesByLanguages(languageEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/sorted/ratingAsc")
    public ResponseEntity<List<GuideSortDto>> getGuidesSortedByRatingAsc() {
        return ResponseEntity.ok(guideService.getAllGuidesSortedByRatingAsc());
    }
    @GetMapping("/sorted/ratingDesc")
    public ResponseEntity<List<GuideSortDto>> getToursSortedByPriceDesc() {
        return ResponseEntity.ok(guideService.getAllGuidesSortedByRatingDesc());
    }
}
