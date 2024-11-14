package com.example.course_work.controller;

import com.example.course_work.dto.ClientCreationDto;
import com.example.course_work.dto.ClientDto;
import com.example.course_work.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/client")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("{id}")
    public ResponseEntity<ClientDto>getById(@PathVariable("id")Long id){
        return ResponseEntity.ok(clientService.getById(id));
    }
       @PostMapping
    public ResponseEntity<ClientDto> addClient(@Valid @RequestBody ClientCreationDto clientCreationDto) {
           return new ResponseEntity<>(clientService.createClient(clientCreationDto), HttpStatus.CREATED);
       }
}
