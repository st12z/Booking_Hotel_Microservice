package com.thuc.rooms.service;

import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.dto.TripTypeDto;

import java.util.List;

public interface ITripService {
    public List<TripDto> getAllTrips(String trip);

    List<TripTypeDto> getAllTripTypes();

    List<String> getDestinationsBySearch(String keyword);
}
