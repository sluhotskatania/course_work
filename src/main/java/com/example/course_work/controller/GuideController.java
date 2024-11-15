package com.example.course_work.controller;

import com.example.course_work.dto.GuideCreationDto;
import com.example.course_work.dto.GuideDto;
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
    public ResponseEntity<List<GuideDto>> getAllGuides() {
        return ResponseEntity.ok(guideService.getAllGuides());
    }
}
