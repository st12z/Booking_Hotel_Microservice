package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.TripConverter;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.entity.Trip;
import com.thuc.rooms.repository.TripRepository;
import com.thuc.rooms.service.ITripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TripServiceImpl implements ITripService {
    private final TripRepository tripRepository;
    @Override
    public List<TripDto> getAllTrips(String trip) {
        List<Trip> trips = tripRepository.findByTripTypeContainingIgnoreCase(trip);
        return trips.stream().map(TripConverter::toTripDto).toList();
    }
}
