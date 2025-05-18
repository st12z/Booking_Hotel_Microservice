package com.thuc.rooms.service;



import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface IPropertyService {
    List<PropertyDto> getAllPropertiesBySlugCity(String slugCity);

    PropertyDto getPropertyBySlug(String slug);

    List<PropertyDto> getPropertiesBySlugs(List<String> slugs);

    PropertyDto getPropertyById(Integer id);

    Integer getAmountProperties();

    PageResponseDto<List<PropertyDto>> getAllProperties(Integer pageNo, Integer pageSize);
}
