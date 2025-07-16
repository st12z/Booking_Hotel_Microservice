package com.thuc.rooms.service;

import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.dto.TripTypeRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITripTypeService {
    PageResponseDto<List<TripTypeDto>> getSearchTripTypes(String keyword, Integer pageNo, Integer pageSize);

    TripTypeDto getTripTypeById(Integer id);

    TripTypeDto updateTripType(Integer id, TripTypeRequestDto tripTypeDto, MultipartFile file);

    TripTypeDto createTripType(TripTypeRequestDto tripTypeDto, MultipartFile file);
}
