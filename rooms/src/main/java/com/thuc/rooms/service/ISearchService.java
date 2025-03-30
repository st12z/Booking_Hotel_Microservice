package com.thuc.rooms.service;

import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface ISearchService {
    // Use NativeQuery
    PageResponseDto<List<PropertyDto>> getPropertiesBySearchV1(int pageNo,int pageSize,SearchDto searchDto);
    // Use JPQL
    PageResponseDto<List<PropertyDto>> getPropertiesBySearchV2(int pageNo,int pageSize,SearchDto searchDto);

}
