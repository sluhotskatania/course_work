package com.example.course_work.controller;

import com.example.course_work.dto.*;
import com.example.course_work.service.ClientService;
import com.example.course_work.service.TourService;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/clients")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("{id}")
    @Cacheable(value = "clients", key = "#id")
    public ResponseEntity<ClientDto>getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(clientService.getById(id));
    }
       @PostMapping
       @CacheEvict(value = "clients", allEntries = true)
       public ResponseEntity<ClientDto> addClient(@Valid @RequestBody ClientCreationDto clientCreationDto) {
           return new ResponseEntity<>(clientService.createClient(clientCreationDto), HttpStatus.CREATED);
       }
    @GetMapping
    @Cacheable(value = "clients")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
    @GetMapping("/sorted")
    public ResponseEntity<Page<ClientDto>> getSortedClients(
            @RequestParam String sortBy,
            @RequestParam String order,
            Pageable pageable) {
        Page<ClientDto> sortedClients = clientService.getSortedClients(sortBy, order, pageable);
        return ResponseEntity.ok(sortedClients);
    }
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
    @PutMapping("/{id}")
    @CacheEvict(value = "clients", allEntries = true)
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @RequestBody @Valid ClientDto clientDto) {
        ClientDto updateClient = clientService.updateClient(id, clientDto);
        return ResponseEntity.ok(updateClient);
    }

}
