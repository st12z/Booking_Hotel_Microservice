package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.CityConverter;
import com.thuc.rooms.converter.TripConverter;
import com.thuc.rooms.converter.TripTypeConverter;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.dto.TripRequestDto;
import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.entity.Trip;
import com.thuc.rooms.entity.TripType;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.repository.TripRepository;
import com.thuc.rooms.repository.TripTypeRepository;
import com.thuc.rooms.service.IRedisDestinationService;
import com.thuc.rooms.service.ITripService;
import com.thuc.rooms.utils.UploadCloudinary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements ITripService {
    private final TripRepository tripRepository;
    private final IRedisDestinationService redisDestinationService;
    private final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);
    private final UploadCloudinary uploadCloudinary;
    private final CityRepository cityRepository;
    private final TripTypeRepository tripTypeRepository;
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
        List<TripType> tripTypes = tripTypeRepository.findAll();
        return tripTypes.stream().map(TripTypeConverter::toTripTypeDto).toList();
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

    @Override
    public PageResponseDto<List<TripDto>> getAllTripsByPage(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM trip tr WHERE 1=1 ");
        if (keyword != null && !keyword.isEmpty()) {
            builder.append(" AND ( unaccent(tr.name) ILIKE unaccent(:keyword) ");
            builder.append(" OR unaccent(tr.trip_type) ILIKE unaccent(:keyword) ");
            builder.append(" OR EXISTS (SELECT 1 FROM cities c WHERE c.id = tr.city_id AND unaccent(c.name) ILIKE unaccent(:keyword)) )");
        }

        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"), Long.class);
        builder.append(" ORDER BY id ASC ");
        Query query = entityManager.createNativeQuery(builder.toString(), Trip.class);
        if(keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword","%"+ keyword+"%");
            queryTotal.setParameter("keyword","%"+ keyword+"%");
        }
        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        List<Trip> trips = query.getResultList();
        long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<TripDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(trips.stream().map(TripConverter::toTripDto).collect(Collectors.toList()))
                .build();

    }

    @Override
    public TripDto getTripById(Integer id) {
        Trip trip = tripRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Trip","id",String.valueOf(id)));
        return TripConverter.toTripDto(trip);
    }
    public void updateGeog(Integer tripId, BigDecimal latitude, BigDecimal longitude){
        StringBuilder builder = new StringBuilder("UPDATE cities SET geog = ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)\n" +
                "    WHERE id = :tripId");
        Map<String,Object> params = new HashMap<>();
        params.put("longitude", latitude);
        params.put("latitude", longitude);
        params.put("tripId", tripId);
        Query query = entityManager.createNativeQuery(builder.toString());
        params.forEach(query::setParameter);
        query.executeUpdate();
    }
    @Transactional
    @Override
    public TripDto updateTrip(Integer id, TripRequestDto tripDto, MultipartFile file) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trip","id",String.valueOf(id)));
        City city = cityRepository.findById(tripDto.getCity_id()).orElseThrow(() -> new ResourceNotFoundException("City","id",String.valueOf(id)));
        String image = uploadCloudinary.uploadCloudinary(file);
        trip.setImage(image);
        trip.setName(tripDto.getName());
        trip.setTripType(tripDto.getTripType());
        trip.setLatitude(tripDto.getLatitude());
        trip.setLongitude(tripDto.getLongitude());
        trip.setCity(city);
        trip = tripRepository.save(trip);
        updateGeog(id, tripDto.getLatitude(), tripDto.getLongitude());
        return TripConverter.toTripDto(trip);
    }
    @Transactional
    @Override
    public TripDto createTrip(TripRequestDto tripDto, MultipartFile file) {
        City city = cityRepository.findById(tripDto.getCity_id()).orElseThrow(() -> new ResourceNotFoundException("City","id",String.valueOf(tripDto.getCity_id())));
        String imageUrl = uploadCloudinary.uploadCloudinary(file);
        Trip trip = Trip.builder()
                .name(tripDto.getName())
                .tripType(tripDto.getTripType())
                .latitude(tripDto.getLatitude())
                .longitude(tripDto.getLongitude())
                .image(imageUrl)
                .city(city)
                .build();
        Trip savedTrip = tripRepository.save(trip);
        updateGeog(savedTrip.getId(),tripDto.getLatitude(), tripDto.getLongitude());
        return TripConverter.toTripDto(savedTrip);
    }
}
