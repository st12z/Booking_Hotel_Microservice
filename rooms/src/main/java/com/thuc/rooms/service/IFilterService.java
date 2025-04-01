package com.thuc.rooms.service;

import com.thuc.rooms.dto.FilterDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface IFilterService {
    PageResponseDto<List<PropertyDto>> filterByCondition(SearchDto searchDto, FilterDto filter,int pageNo,int pageSize);
}
