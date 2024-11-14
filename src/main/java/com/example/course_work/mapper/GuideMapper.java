package com.example.course_work.mapper;

import com.example.course_work.dto.GuideCreationDto;
import com.example.course_work.dto.GuideDto;
import com.example.course_work.entity.Guide;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GuideMapper {
    Guide toEntity(GuideDto guideDto);

    GuideDto toDto(Guide guide);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Guide partialUpdate(GuideDto guideDto, @MappingTarget Guide guide);

    Guide toEntity(GuideCreationDto guideCreationDto);
}