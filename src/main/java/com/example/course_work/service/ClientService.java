package com.example.course_work.service;

import com.example.course_work.dto.ClientCreationDto;
import com.example.course_work.dto.ClientDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Client;
import com.example.course_work.mapper.BookingMapper;
import com.example.course_work.mapper.ClientMapper;
import com.example.course_work.repository.BookingRepository;
import com.example.course_work.repository.ClientRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional(readOnly=true)
    public ClientDto getById(Long id){
        Client client =clientRepository.findById(id).orElseThrow();
       return clientMapper.toDto(client);
    }
    public ClientDto createClient(ClientCreationDto client) {
        return clientMapper.toDto(clientRepository.save(clientMapper.toEntity(client)));
    }
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<ClientDto> getClientsByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<ClientDto> getClientsBySurname(String surname) {
        return clientRepository.findBySurnameContainingIgnoreCase(surname)
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }
    public List<ClientDto> getClientsByNameAndSurname(String name, String surname) {
        List<Client> clients;

        if (name != null && surname != null) {
            clients = clientRepository.findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(name, surname);
        } else if (name != null) {
            clients = clientRepository.findByNameContainingIgnoreCase(name);
        } else if (surname != null) {
            clients = clientRepository.findBySurnameContainingIgnoreCase(surname);
        } else {
            clients = clientRepository.findAll();
        }

        return clients.stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

}
