package com.example.course_work.mapper;

import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Booking;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    @Mapping(source = "tourId", target = "tour.id")
    @Mapping(source = "clientId", target = "client.id")
    Booking toEntity(BookingDto bookingDto);

    @InheritInverseConfiguration(name = "toEntity")
    BookingDto toDto(Booking booking);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking partialUpdate(BookingDto bookingDto, @MappingTarget Booking booking);

    Booking toEntity(BookingCreationDto bookingCreationDto);
}