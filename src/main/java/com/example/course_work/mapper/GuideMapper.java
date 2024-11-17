package com.example.course_work.mapper;

import com.example.course_work.dto.GuideCreationDto;
import com.example.course_work.dto.GuideDto;
import com.example.course_work.dto.GuideSortDto;
import com.example.course_work.entity.Guide;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GuideMapper {
    @Mapping(source = "tourType", target = "tour.type")
    @Mapping(source = "tourReturnDate", target = "tour.returnDate")
    @Mapping(source = "tourDepartureDate", target = "tour.departureDate")
    @Mapping(source = "tourDuration", target = "tour.duration")
    @Mapping(source = "tourDestination", target = "tour.destination")
    @Mapping(source = "tourName", target = "tour.name")
    @Mapping(source = "tourId", target = "tour.id")
    Guide toEntity(GuideDto guideDto);

    @Mapping(source = "tour.type", target = "tourType")
    @Mapping(source = "tour.returnDate", target = "tourReturnDate")
    @Mapping(source = "tour.departureDate", target = "tourDepartureDate")
    @Mapping(source = "tour.duration", target = "tourDuration")
    @Mapping(source = "tour.destination", target = "tourDestination")
    @Mapping(source = "tour.name", target = "tourName")
    @Mapping(source = "tour.id", target = "tourId")
    GuideDto toDto(Guide guide);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Guide partialUpdate(GuideDto guideDto, @MappingTarget Guide guide);

    Guide toEntity(GuideCreationDto guideCreationDto);
    GuideSortDto toGdSortDto(Guide guide);
}