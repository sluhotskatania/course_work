package com.example.course_work.mapper;

import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Booking;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    @Mapping(source = "tourType", target = "tour.type")
    @Mapping(source = "tourReturnDate", target = "tour.returnDate")
    @Mapping(source = "tourDepartureDate", target = "tour.departureDate")
    @Mapping(source = "tourPrice", target = "tour.price")
    @Mapping(source = "tourName", target = "tour.name")
    @Mapping(source = "tourId", target = "tour.id")
    @Mapping(source = "clientBirthDate", target = "client.birthDate")
    @Mapping(source = "clientPhone", target = "client.phone")
    @Mapping(source = "clientEmail", target = "client.email")
    @Mapping(source = "clientSurname", target = "client.surname")
    @Mapping(source = "clientName", target = "client.name")
    @Mapping(source = "clientId", target = "client.id")
    Booking toEntity(BookingDto bookingDto);

    @InheritInverseConfiguration(name = "toEntity")
    BookingDto toDto(Booking booking);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking partialUpdate(BookingDto bookingDto, @MappingTarget Booking booking);
}