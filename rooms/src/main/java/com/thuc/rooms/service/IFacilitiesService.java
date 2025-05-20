package com.thuc.rooms.service;

import com.thuc.rooms.dto.FacilitiesDto;
import com.thuc.rooms.entity.Facilities;

import java.util.List;

public interface IFacilitiesService {
    List<FacilitiesDto> geAllFacilities();
}
