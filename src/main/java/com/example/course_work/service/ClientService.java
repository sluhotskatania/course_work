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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@AllArgsConstructor
@Transactional
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional(readOnly = true)
    public ClientDto getById(Long id) {
        logger.info("Fetching client by ID: {}", id);
        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new ClientNotFound("Client not found"));
            logger.info("Client with ID {} fetched successfully", id);
            return clientMapper.toDto(client);
        } catch (ClientNotFound e) {
            logger.warn("Client with ID {} not found", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching client by ID: {}", id, e);
            throw e;
        }
    }
    @Transactional
    public ClientDto createClient(ClientCreationDto client) {
        logger.info("Creating a new client with data: {}", client);
        try {
            Client savedClient = clientRepository.save(clientMapper.toEntity(client));
            logger.info("Client created successfully with ID: {}", savedClient.getId());
            return clientMapper.toDto(savedClient);
        } catch (Exception e) {
            logger.error("Error while creating a new client: {}", client, e);
            throw e;
        }
    }
    @Transactional(readOnly = true)
    public Page<ClientDto> getAllClients(Pageable pageable) {
        logger.info("Fetching clients with pagination");
        try {
            Page<ClientDto> clients = clientRepository.findAll(pageable)
                    .map(clientMapper::toDto);
            logger.info("Successfully fetched {} clients on page {}", clients.getNumberOfElements(), pageable.getPageNumber());
            return clients;
        } catch (Exception e) {
            logger.error("Error while fetching paginated clients", e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Page<ClientDto> getSortedClients(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted clients with sortBy: {}, order: {}, page: {}", sortBy, order, pageable);
        try {
            Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
            Page<Client> clients = clientRepository.findAll(sortedPageable);

            Page<ClientDto> clientDtos = clients.map(client -> new ClientDto(
                    client.getId(), client.getCreated(), client.getName(), client.getSurname(),
                    client.getEmail(), client.getPhone(), client.getBirthDate()
            ));

            logger.info("Successfully fetched sorted clients: total elements = {}, total pages = {}",
                    clientDtos.getTotalElements(), clientDtos.getTotalPages());
            return clientDtos;
        } catch (Exception e) {
            logger.error("Error while fetching sorted clients with sortBy: {}, order: {}, page: {}", sortBy, order, pageable, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> getFilteredClients(String name, String surname, String email, String phone, Date minBirthDate, Date maxBirthDate, Pageable pageable) {
        logger.info("Fetching filtered clients with parameters - name: {}, surname: {}, email: {}, phone: {}, minBirthDate: {}, maxBirthDate: {}, page: {}",
                name, surname, email, phone, minBirthDate, maxBirthDate, pageable);

        try {
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
            logger.info("Successfully fetched {} clients, total pages: {}", clients.getTotalElements(), clients.getTotalPages());

            return clients.map(client -> new ClientDto(client.getId(), client.getCreated(), client.getName(),
                    client.getSurname(), client.getEmail(), client.getPhone(), client.getBirthDate()));
        } catch (Exception e) {
            logger.error("Error while fetching filtered clients with parameters - name: {}, surname: {}, email: {}, phone: {}, minBirthDate: {}, maxBirthDate: {}, page: {}",
                    name, surname, email, phone, minBirthDate, maxBirthDate, pageable, e);
            throw e;
        }
    }

    @Transactional
    public ClientDto updateClient(Long id, ClientDto clientDto) {
        logger.info("Updating client with ID: {} using provided data: {}", id, clientDto);

        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new ClientNotFound("Client not found"));
            clientMapper.partialUpdate(clientDto, client);

            Client updatedClient = clientRepository.save(client);
            logger.info("Client with ID: {} successfully updated", id);

            return clientMapper.toDto(updatedClient);
        } catch (ClientNotFound e) {
            logger.warn("Client with ID: {} not found for update", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating client with ID: {}", id, e);
            throw e;
        }
    }


}
