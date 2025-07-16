package com.thuc.rooms.service;

import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.dto.TripRequestDto;
import com.thuc.rooms.dto.TripTypeDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITripService {
    public List<TripDto> getAllTrips(String trip);

    List<TripTypeDto> getAllTripTypes();

    List<String> getDestinationsBySearch(String keyword);

    List<Object> getDestinationsBySuggest(List<String> destinations);

    PageResponseDto<List<TripDto>> getAllTripsByPage(String keyword, Integer pageNo, Integer pageSize);

    TripDto getTripById(Integer id);

    TripDto updateTrip(Integer id, TripRequestDto tripDto, MultipartFile file);

    TripDto createTrip(TripRequestDto tripDto, MultipartFile file);
}
