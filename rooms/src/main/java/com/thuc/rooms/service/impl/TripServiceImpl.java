package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.TripConverter;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.entity.Trip;
import com.thuc.rooms.repository.TripRepository;
import com.thuc.rooms.service.ITripService;
import com.thuc.rooms.utils.TripEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TripServiceImpl implements ITripService {
    private final TripRepository tripRepository;
    private final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);
    @Override
    public List<TripDto> getAllTrips(String trip) {
        log.debug("Requested to get all trips for {} successfully", trip);
        List<Trip> trips = tripRepository.findByTripTypeContainingIgnoreCase(trip);
        return trips.stream().map(TripConverter::toTripDto).toList();
    }

    @Override
    public List<TripTypeDto> getAllTripTypes() {
        return TripEnum.getTrips();
    }
}
