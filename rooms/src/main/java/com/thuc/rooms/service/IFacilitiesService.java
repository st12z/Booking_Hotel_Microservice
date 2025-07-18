package com.thuc.rooms.service;

import com.thuc.rooms.dto.FacilitiesDto;
import com.thuc.rooms.dto.FacilityDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.entity.Facilities;

import java.util.List;

public interface IFacilitiesService {
    List<FacilitiesDto> geAllFacilities();

    PageResponseDto<List<FacilitiesDto>> getAllFacilitiesPage(String keyword, Integer pageNo, Integer pageSize);

    FacilitiesDto getFacilityById(Integer id);

    FacilitiesDto update(Integer id, FacilityDto facilityDto);

    FacilitiesDto create(FacilityDto facilityDto);
}
