package com.thuc.rooms.service;

import com.thuc.rooms.dto.FilterPropertiesDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface IFilterService {
    PageResponseDto<List<PropertyDto>> filterByCondition(SearchDto searchDto, FilterPropertiesDto filter, int pageNo, int pageSize);
}
