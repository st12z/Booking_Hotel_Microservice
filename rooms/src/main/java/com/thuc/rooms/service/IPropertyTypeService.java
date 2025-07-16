package com.thuc.rooms.service;

import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyTypeDto;
import com.thuc.rooms.dto.PropertyTypeRequestDto;

import java.util.List;

public interface IPropertyTypeService {
    List<String> getAllPropertyTypes();

    PageResponseDto<List<PropertyTypeDto>> getAllPropertyTypesByPage(String keyword, Integer pageNo, Integer pageSize);

    PropertyTypeDto getPropertyTypeById(Integer id);

    PropertyTypeDto updatePropertyType(Integer id, PropertyTypeRequestDto propertyTypeDto);

    PropertyTypeDto createPropertyType(PropertyTypeRequestDto propertyTypeDto);
}
