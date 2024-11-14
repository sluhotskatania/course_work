package com.example.course_work.mapper;

import com.example.course_work.dto.TourCreationDto;
import com.example.course_work.dto.TourDto;
import com.example.course_work.entity.Tour;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TourMapper {
    Tour toEntity(TourDto tourDto);

    TourDto toDto(Tour tour);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tour partialUpdate(TourDto tourDto, @MappingTarget Tour tour);

    Tour toEntity(TourCreationDto tourCreationDto);
}