package com.example.course_work.controller;

import com.example.course_work.dto.ClientCreationDto;
import com.example.course_work.dto.ClientDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.service.ClientService;
import com.example.course_work.service.TourService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
       @GetMapping
       public ResponseEntity<List<ClientDto>> getAllClients() {
           return ResponseEntity.ok(clientService.getAllClients());
    }
    @GetMapping("/filter/name")
    public ResponseEntity<List<ClientDto>> getClientsByName(@RequestParam String name) {
        return ResponseEntity.ok(clientService.getClientsByName(name));
    }
    @GetMapping("/filter/surname")
    public ResponseEntity<List<ClientDto>> getClientsBySurname(@RequestParam String surname) {
        return ResponseEntity.ok(clientService.getClientsBySurname(surname));
    }
    @GetMapping("/filter/name-surname")
    public ResponseEntity<List<ClientDto>> getClientsByNameAndSurname(@RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) String surname) {
        return ResponseEntity.ok(clientService.getClientsByNameAndSurname(name, surname));
    }

}
