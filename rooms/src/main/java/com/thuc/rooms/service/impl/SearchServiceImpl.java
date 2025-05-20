package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.BookingRoomsDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.Room;
import com.thuc.rooms.entity.RoomType;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.repository.RoomRepository;
import com.thuc.rooms.repository.TripRepository;
import com.thuc.rooms.service.ISearchService;
import com.thuc.rooms.service.IRedisPropertyService;
import com.thuc.rooms.service.client.BookingsFeignClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements ISearchService {
    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final TripRepository tripRepository;
    private final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final IRedisPropertyService redisService;
    private final RoomRepository roomRepository;
    private final BookingsFeignClient bookingsFeignClient;
    @PersistenceContext
    private EntityManager entityManager;
    // Use NativeQuery
    @Override
    public PageResponseDto<List<PropertyDto>> getPropertiesBySearchV1(int pageNo,int pageSize,SearchDto searchDto) {
        log.debug("Requested to getPropertiesBySearchV1 with {} successfully",searchDto);
        String key = String.format("search:%s",searchDto);
        StringBuilder sql = new StringBuilder(" SELECT * FROM properties p WHERE 1=1 ");
        Map<String,Object> parameters = new HashMap<>();
        if(searchDto.getDestination() != null && !searchDto.getDestination().isEmpty()) {
            sql.append(" AND (unaccent(p.name) ILIKE unaccent(:destination) ");
            sql.append(" OR EXISTS ( SELECT 1 FROM cities c WHERE p.city_id = c.id and unaccent(name) ILIKE unaccent(:destination)) ");
            sql.append(" OR EXISTS (SELECT 1 FROM trip tr WHERE  p.city_id = tr.city_id and unaccent(name) ILIKE unaccent(:destination)))");
            parameters.put("destination",(String)('%'+searchDto.getDestination()+'%'));
        }

        if(searchDto.getQuantityBeds()!=null){
            sql.append(" AND EXISTS (SELECT 1 FROM room_type rt WHERE rt.property_id=p.id AND num_beds = :quantityBed) ");
            parameters.put("quantityBed",searchDto.getQuantityBeds());
        }
        Query countQuery = entityManager.createNativeQuery(sql.toString().replace("SELECT *","SELECT COUNT(*)"));
        parameters.forEach(countQuery::setParameter);
        Long total = (Long)countQuery.getSingleResult();
        Query allQuery = entityManager.createNativeQuery(sql.toString(),Property.class);
        parameters.forEach(allQuery::setParameter);
        List<Property> propertiesAll = allQuery.getResultList();
        List<PropertyDto> propertyDtoAll = propertiesAll.stream().map(PropertyConverter::toPropertyDto).toList();

        sql.append(" LIMIT :limit OFFSET :offset ");
        int limit = pageSize;
        int offset = pageSize*(pageNo-1);
        Query query = entityManager.createNativeQuery(sql.toString(), Property.class);
        parameters.forEach(query::setParameter);
        query.setParameter("limit",limit);
        query.setParameter("offset",offset);
        List<Property> properties = query.getResultList();
        LocalDateTime currentTime = LocalDateTime.now();
        log.debug("properties :{}",properties);
        List<Property> propertiesByTime = properties.stream().filter(property -> {
            List<RoomType> roomTypes = property.getRoomTypes();
            int propertyId= property.getId();
            return roomTypes.stream().anyMatch(roomType -> {
                int roomTypeId = roomType.getId();
                List<Integer> roomNumbers = roomRepository.findByRoomTypeIdAndPropertyId(roomTypeId,propertyId).stream().map(Room::getRoomNumber).toList();
                List<BookingRoomsDto> bookingRoomsDtos = bookingsFeignClient.getBookingRooms(roomTypeId,propertyId).getBody().getData();
                Set<Integer> bookedNow = new HashSet<>();
                if(!bookingRoomsDtos.isEmpty()){
                    for(BookingRoomsDto bookingRoomsDto : bookingRoomsDtos){
                        LocalDateTime checkInRoom = bookingRoomsDto.getCheckIn();
                        LocalDateTime checkOutRoom = bookingRoomsDto.getCheckOut();
                        if(searchDto.getCheckIn()!=null && searchDto.getCheckOut()!=null){
                            if(!(searchDto.getCheckOut().isBefore(checkInRoom) || searchDto.getCheckIn().isAfter(checkOutRoom))){
                                bookedNow.addAll(bookingRoomsDto.getNumRooms());
                            }
                        }
                        else{
                            if(!(currentTime.isBefore(checkInRoom) || currentTime.isAfter(checkOutRoom))){
                                bookedNow.addAll(bookingRoomsDto.getNumRooms());
                            }
                        }
                    }
                }
                return roomNumbers.stream().anyMatch(number->!bookedNow.contains(number));
            });
        }).toList();
        List<PropertyDto> propertyDtos = propertiesByTime.stream().map(PropertyConverter::toPropertyDto).collect(Collectors.toList());
        if(redisService.getData(key)==null) redisService.saveData(key,propertyDtos);
        return PageResponseDto.<List<PropertyDto>>builder()
                .dataPage(propertyDtos)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .build();

    }
    // Use JPQL
    @Override
    public PageResponseDto<List<PropertyDto>> getPropertiesBySearchV2(int pageNo,int pageSize,SearchDto searchDto) {
        log.info("Requested to getPropertiesBySearchV2 with {} successfully",searchDto);
        StringBuilder sql = new StringBuilder(" SELECT p FROM Property p WHERE 1=1 ");
        Map<String,Object> parameters = new HashMap<>();
        if(searchDto.getDestination() != null && !searchDto.getDestination().isEmpty()) {
            sql.append(" AND (LOWER(CAST(UNACCENT(p.name) AS text)) LIKE LOWER(CAST(UNACCENT(:destination) AS text)) ");
            sql.append(" OR LOWER(CAST(UNACCENT(p.city.name) AS text)) LIKE LOWER(CAST(UNACCENT(:destination) AS text)) ");
            sql.append(" OR EXISTS(SELECT 1 FROM Trip tr WHERE tr.city = p.city AND LOWER(CAST(UNACCENT(tr.name) AS text)) LIKE LOWER(CAST(UNACCENT(:destination) AS text)) )) ");
            parameters.put("destination", "%" + searchDto.getDestination() + "%");

        }
        if(searchDto.getCheckOut() !=null && searchDto.getCheckIn()!=null) {
            sql.append(" AND EXISTS(SELECT 1 FROM Room r WHERE r.property=p AND (r.checkOut<:checkIn OR (r.checkOut is null AND r.checkIn is null)))");
            parameters.put("checkIn",searchDto.getCheckIn());
        }
        if(searchDto.getQuantityBeds()!=null){
            sql.append(" AND EXISTS(SELECT 1 FROM RoomType rt WHERE rt.property=p AND rt.numBeds = :quantityBed) ");
            parameters.put("quantityBed",searchDto.getQuantityBeds());
        }
        Query countQuery = entityManager.createQuery(sql.toString().replace("SELECT p","SELECT COUNT(p)"),Long.class);
        parameters.forEach(countQuery::setParameter);
        Long total = (Long)countQuery.getSingleResult();
        Query query = entityManager.createQuery(sql.toString(),Property.class);
        int limit = pageSize;
        int offset = pageSize*(pageNo-1);
        parameters.forEach(query::setParameter);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        List<Property> properties = query.getResultList();
        List<PropertyDto> propertyDtos = properties.stream().map(PropertyConverter::toPropertyDto).toList();
        return PageResponseDto.<List<PropertyDto>>builder()
                .dataPage(propertyDtos)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .build();

    }

}