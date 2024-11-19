package com.example.course_work.service;

import com.example.course_work.dto.ClientCreationDto;
import com.example.course_work.dto.ClientDto;
import com.example.course_work.entity.Client;
import com.example.course_work.exception.ClientNotFound;
import com.example.course_work.mapper.BookingMapper;
import com.example.course_work.mapper.ClientMapper;
import com.example.course_work.repository.BookingRepository;
import com.example.course_work.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional(readOnly=true)
    @Cacheable(value = "clients", key = "#id")
    public ClientDto getById(Long id){
        Client client =clientRepository.findById(id).orElseThrow();
       return clientMapper.toDto(client);
    }
    public ClientDto createClient(ClientCreationDto client) {
        return clientMapper.toDto(clientRepository.save(clientMapper.toEntity(client)));
    }
    @Transactional(readOnly = true)
    @Cacheable(value = "clients")
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> getSortedClients(String sortBy, String order, Pageable pageable) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        Page<Client> clients = clientRepository.findAll(sortedPageable);
        return clients.map(client -> new ClientDto(
                client.getId(), client.getCreated(), client.getName(), client.getSurname(),
                client.getEmail(), client.getPhone(), client.getBirthDate()
        ));
    }
    @Transactional(readOnly = true)
    public Page<ClientDto> getFilteredClients(String name, String surname, String email, String phone, Date minBirthDate, Date maxBirthDate, Pageable pageable) {
        Specification<Client> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (surname != null && !surname.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%"));
        }
        if (email != null && !email.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
        }
        if (minBirthDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("birthDate"), minBirthDate));
        }
        if (maxBirthDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("birthDate"), maxBirthDate));
        }

        Page<Client> clients = clientRepository.findAll(specification, pageable);
        return clients.map(client -> new ClientDto(client.getId(), client.getCreated(), client.getName(),
                client.getSurname(), client.getEmail(), client.getPhone(), client.getBirthDate()));
    }
    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFound("Client not found"));
        clientMapper.partialUpdate(clientDto, client);
        return clientMapper.toDto(clientRepository.save(client));
    }


}
