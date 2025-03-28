package com.thuc.rooms.service;

import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface ISearchService {
    List<PropertyDto> getPropertiesBySearch(SearchDto searchDto);
}
