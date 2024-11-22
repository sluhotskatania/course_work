package com.example.course_work.controller;

import com.example.course_work.dto.*;
import com.example.course_work.enums.LanguagesEnum;
import com.example.course_work.service.GuideService;
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
@RequestMapping("api/guides")
public class GuideController {
    private final GuideService guideService;

    @Operation(
            summary = "Get guide by ID",
            description = "Fetches guide details based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched guide",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuideDto.class))),
                    @ApiResponse(responseCode = "404", description = "Guide not found")
            }
    )
    @GetMapping("{id}")
    @Cacheable(value = "guides", key = "#id")
    public ResponseEntity<GuideDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(guideService.getById(id));
    }

    @Operation(
            summary = "Add new guide",
            description = "Creates a new guide in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Guide created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuideDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    @CacheEvict(value = "guides", allEntries = true)
    public ResponseEntity<GuideDto> addGuide(@Valid @RequestBody GuideCreationDto guideCreationDto) {
        return new ResponseEntity<>(guideService.createGuide(guideCreationDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all guides",
            description = "Fetches a paginated list of all guides with optional sorting",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched guides",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuideSortDto.class)))
            }
    )
    @GetMapping
    @Cacheable(value = "guides")
    public ResponseEntity<Page<GuideSortDto>> getGuides(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String[] sort) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<GuideSortDto> guides = guideService.getAllGuides(pageable);
        return ResponseEntity.ok(guides);
    }

    @Operation(
            summary = "Get sorted guides",
            description = "Fetches a paginated and sorted list of guides based on sorting criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched sorted guides",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuideDto.class)))
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<Page<GuideDto>> getSortedGuides(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            Pageable pageable) {
        Page<GuideDto> sortedGuides = guideService.getSortedGuides(sortBy, order, pageable);
        return ResponseEntity.ok(sortedGuides);
    }

    @Operation(
            summary = "Get filtered guides",
            description = "Fetches a filtered and paginated list of guides based on the provided filter criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched filtered guides",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuideDto.class)))
            }
    )
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

    @Operation(
            summary = "Update guide by ID",
            description = "Updates the details of an existing guide based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Guide successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuideDto.class))),
                    @ApiResponse(responseCode = "404", description = "Guide not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "guides", allEntries = true)
    public ResponseEntity<GuideDto> updateGuide(@PathVariable Long id, @RequestBody @Valid GuideDto guideDto) {
        GuideDto updateGuide = guideService.updateGuide(id, guideDto);
        return ResponseEntity.ok(updateGuide);
    }
}


