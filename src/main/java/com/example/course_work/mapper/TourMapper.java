package com.example.course_work.mapper;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Tour;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TourMapper {
    @Mapping(source = "accommodationId", target = "accommodation.id")
    @Mapping(source = "bookingId", target = "booking.id")
    Tour toEntity(TourDto tourDto);

    @Mapping(source = "accommodation.id", target = "accommodationId")
    @Mapping(source = "booking.id", target = "bookingId")
    TourDto toDto(Tour tour);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tour partialUpdate(TourDto tourDto, @MappingTarget Tour tour);

    Tour toEntity(TourCreationDto tourCreationDto);
}