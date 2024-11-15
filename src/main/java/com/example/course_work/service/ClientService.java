package com.example.course_work.service;

import com.example.course_work.dto.ClientCreationDto;
import com.example.course_work.dto.ClientDto;
import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Client;
import com.example.course_work.entity.Tour;
import com.example.course_work.mapper.ClientMapper;
import com.example.course_work.repository.BookingRepository;
import com.example.course_work.repository.ClientRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BookingRepository bookingRepository;

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
}
