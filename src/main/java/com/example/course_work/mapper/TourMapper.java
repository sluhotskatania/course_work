package com.example.course_work.mapper;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.dto.TourSortDto;
import com.example.course_work.entity.Tour;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TourMapper {
    @Mapping(source = "bookingNotes", target = "booking.notes")
    @Mapping(source = "bookingTotalPrice", target = "booking.totalPrice")
    @Mapping(source = "accommodationPricePerNight", target = "accommodation.pricePerNight")
    @Mapping(source = "accommodationType", target = "accommodation.type")
    @Mapping(source = "accommodationLocation", target = "accommodation.location")
    @Mapping(source = "accommodationName", target = "accommodation.name")
    @Mapping(source = "accommodationId", target = "accommodation.id")
    @Mapping(source = "bookingId", target = "booking.id")
    Tour toEntity(TourDto tourDto);

    @Mapping(source = "booking.notes", target = "bookingNotes")
    @Mapping(source = "booking.totalPrice", target = "bookingTotalPrice")
    @Mapping(source = "accommodation.pricePerNight", target = "accommodationPricePerNight")
    @Mapping(source = "accommodation.type", target = "accommodationType")
    @Mapping(source = "accommodation.location", target = "accommodationLocation")
    @Mapping(source = "accommodation.name", target = "accommodationName")
    @Mapping(source = "accommodation.id", target = "accommodationId")
    @Mapping(source = "booking.id", target = "bookingId")
    TourDto toDto(Tour tour);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tour partialUpdate(TourDto tourDto, @MappingTarget Tour tour);

    Tour toEntity(TourCreationDto tourCreationDto);

    @Mapping(source = "destination", target = "destination")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "duration", target = "duration")
    TourSortDto toSortDto(Tour tour);
}