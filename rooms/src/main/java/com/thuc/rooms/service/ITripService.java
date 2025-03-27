package com.thuc.rooms.service;

import com.thuc.rooms.dto.TripDto;

import java.util.List;

public interface ITripService {
    public List<TripDto> getAllTrips(String trip);
}
