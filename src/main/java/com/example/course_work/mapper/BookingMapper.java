package com.example.course_work.mapper;

import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Booking;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    @Mapping(source = "clientPhone", target = "client.phone")
    @Mapping(source = "clientEmail", target = "client.email")
    @Mapping(source = "clientSurname", target = "client.surname")
    @Mapping(source = "clientName", target = "client.name")
    @Mapping(source = "accommodationLocation", target = "accommodation.location")
    @Mapping(source = "accommodationName", target = "accommodation.name")
    Booking toEntity(BookingDto bookingDto);

    @InheritInverseConfiguration(name = "toEntity")
    BookingDto toDto(Booking booking);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking partialUpdate(BookingDto bookingDto, @MappingTarget Booking booking);
    @Mapping(source = "nights", target = "nights")
    Booking toEntity(BookingCreationDto bookingCreationDto);
}