package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.CityConverter;
import com.thuc.rooms.converter.TripConverter;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.entity.Trip;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.repository.TripRepository;
import com.thuc.rooms.service.IRedisDestinationService;
import com.thuc.rooms.service.ITripService;
import com.thuc.rooms.utils.TripEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements ITripService {
    private final TripRepository tripRepository;
    private final IRedisDestinationService redisDestinationService;
    private final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
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

    // tra ve goi y khi search
    @Override
    public List<String> getDestinationsBySearch(String keyword) {
        log.debug("Requested to get all destinations suggest when search for {} successfully", keyword);
        String sqlTrips = "SELECT name FROM trip WHERE UNACCENT(name) ILIKE UNACCENT(:keyword)";
        String sqlCtities = "SELECT name FROM cities WHERE UNACCENT(name) ILIKE UNACCENT(:keyword)";
        Query queryTrips = entityManager.createNativeQuery(sqlTrips,String.class);
        Query queryCtities= entityManager.createNativeQuery(sqlCtities,String.class);
        queryTrips.setParameter("keyword","%"+ keyword+"%");
        queryCtities.setParameter("keyword","%"+ keyword+"%");
        List<String> trips = queryTrips.getResultList();
        List<String> cities = queryCtities.getResultList();
        return Stream.concat(trips.stream(), cities.stream()).distinct().toList();
    }
    // Tim kiem diem den gan day
    @Override
    public List<Object> getDestinationsBySuggest(List<String> destinations) {
        log.debug("Requested to get all destinations lately after search for {} successfully", destinations);
        String key = String.join("_", destinations);
        List<Object> destinationsSuggest = new ArrayList<>();
        if(redisDestinationService.getData(key)!=null && !redisDestinationService.getData(key).isEmpty()){
            return redisDestinationService.getData(key);
        }
        if(destinations != null && !destinations.isEmpty()) {
            for(String destination : destinations){
                StringBuilder sqlCity = new StringBuilder(" SELECT * FROM cities WHERE UNACCENT(name) ILIKE UNACCENT(:destination)");
                StringBuilder sqlTrip = new StringBuilder(" SELECT * FROM trip WHERE UNACCENT(name) ILIKE UNACCENT(:destination)");
                Query queryCity = entityManager.createNativeQuery(sqlCity.toString(), City.class);
                Query queryTrip = entityManager.createNativeQuery(sqlTrip.toString(), Trip.class);
                queryCity.setParameter("destination","%"+ destination+"%");
                queryTrip.setParameter("destination","%"+ destination+"%");
                List<City> cities = queryCity.getResultList();
                List<Trip> trips = queryTrip.getResultList();
                if(!cities.isEmpty()){
                    destinationsSuggest.addAll(cities.stream().map(CityConverter::toCityDto).distinct().toList());
                }
                if(!trips.isEmpty()){
                    destinationsSuggest.addAll(trips.stream().map(TripConverter::toTripDto).distinct().toList());
                }

            }
            redisDestinationService.saveData(key, destinationsSuggest);
        }
        return destinationsSuggest.stream().distinct().toList();
    }
}
