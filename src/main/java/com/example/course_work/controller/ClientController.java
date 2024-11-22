package com.example.course_work.controller;

import com.example.course_work.dto.*;
import com.example.course_work.service.ClientService;
import com.example.course_work.service.TourService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/clients")
public class ClientController {
    private final ClientService clientService;

    @Operation(
            summary = "Get client by ID",
            description = "Fetches a client's details based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched client",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientDto.class))),
                    @ApiResponse(responseCode = "404", description = "Client not found")
            }
    )
    @GetMapping("{id}")
    @Cacheable(value = "clients", key = "#id")
    public ResponseEntity<ClientDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @Operation(
            summary = "Add a new client",
            description = "Creates a new client in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Client successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid client data provided")
            }
    )
    @PostMapping
    @CacheEvict(value = "clients", allEntries = true)
    public ResponseEntity<ClientDto> addClient(@Valid @RequestBody ClientCreationDto clientCreationDto) {
        return new ResponseEntity<>(clientService.createClient(clientCreationDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get a paginated list of all clients",
            description = "Fetches all clients with pagination support",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched clients",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
            }
    )
    @GetMapping
    @Cacheable(value = "clients")
    public ResponseEntity<Page<ClientDto>> getClients(
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientDto> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @Operation(
            summary = "Get a sorted list of clients",
            description = "Fetches clients sorted by the specified field and order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched sorted clients",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid sorting parameters")
            }
    )
    @GetMapping("/sorted")
    public ResponseEntity<Page<ClientDto>> getSortedClients(
            @RequestParam String sortBy,
            @RequestParam String order,
            Pageable pageable) {
        Page<ClientDto> sortedClients = clientService.getSortedClients(sortBy, order, pageable);
        return ResponseEntity.ok(sortedClients);
    }

    @Operation(
            summary = "Get a filtered list of clients",
            description = "Fetches clients based on optional filters like name, surname, email, phone, and birth date range",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched filtered clients",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid filter parameters")
            }
    )
    @GetMapping("/filtered")
    public ResponseEntity<Page<ClientDto>> getFilteredClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date minBirthDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date maxBirthDate,
            Pageable pageable) {

        Page<ClientDto> filteredClients = clientService.getFilteredClients(name, surname, email, phone, minBirthDate, maxBirthDate, pageable);
        return ResponseEntity.ok(filteredClients);
    }

    @Operation(
            summary = "Update client details",
            description = "Updates an existing client's details based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientDto.class))),
                    @ApiResponse(responseCode = "404", description = "Client not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid client data provided")
            }
    )
    @PutMapping("/{id}")
    @CacheEvict(value = "clients", allEntries = true)
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @RequestBody @Valid ClientDto clientDto) {
        ClientDto updateClient = clientService.updateClient(id, clientDto);
        return ResponseEntity.ok(updateClient);
    }
}
