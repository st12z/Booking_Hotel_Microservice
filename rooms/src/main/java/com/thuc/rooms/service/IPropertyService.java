package com.thuc.rooms.service;



import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface IPropertyService {
    List<PropertyDto> getAllProperties(String slugCity);

    PropertyDto getPropertyBySlug(String slug);
}
