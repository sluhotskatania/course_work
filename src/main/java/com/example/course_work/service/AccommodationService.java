package com.example.course_work.service;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.enums.TypeAccommodationEnum;
import com.example.course_work.enums.TypeEnum;
import com.example.course_work.mapper.AccommodationMapper;
import com.example.course_work.repository.AccommodationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Transactional(readOnly = true)
    public AccommodationDto getById(Long id){
        Accommodation accommodation =accommodationRepository.findById(id).orElseThrow();
        return accommodationMapper.toDto(accommodation);
    }
    public AccommodationDto createAccommodation(AccommodationCreationDto accommodation) {
        return accommodationMapper.toDto(accommodationRepository.save(accommodationMapper.toEntity(accommodation)));
    }
    @Transactional(readOnly = true)
    public List<AccommodationDto> getAllAccommodations() {
        return accommodationRepository.findAll().stream()
                .map(accommodationMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<AccommodationDto> getTourAccommodationsByType(TypeAccommodationEnum type) {
        return accommodationRepository.findByType(type)  // Використовуємо правильний метод репозиторію
                .stream()
                .map(accommodationMapper::toDto)  // Конвертуємо в DTO
                .collect(Collectors.toList());  // Збираємо результат в список
    }
    @Transactional(readOnly = true)
    public List<AccommodationDto> getAccommodationsByLocation(String location) {
        return accommodationRepository.findByLocationContainingIgnoreCase(location)
                .stream()
                .map(accommodationMapper::toDto)
                .toList();
    }
}
