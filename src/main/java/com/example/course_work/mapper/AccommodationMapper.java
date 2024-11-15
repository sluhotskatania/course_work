package com.example.course_work.mapper;

import com.example.course_work.dto.AccommodationCreationDto;
import com.example.course_work.dto.AccommodationDto;
import com.example.course_work.entity.Accommodation;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccommodationMapper {
    Accommodation toEntity(AccommodationDto accommodationDto);

    @InheritInverseConfiguration(name = "toEntity")
    AccommodationDto toDto(Accommodation accommodation);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Accommodation partialUpdate(AccommodationDto accommodationDto, @MappingTarget Accommodation accommodation);

    Accommodation toEntity(AccommodationCreationDto accommodationCreationDto);
}