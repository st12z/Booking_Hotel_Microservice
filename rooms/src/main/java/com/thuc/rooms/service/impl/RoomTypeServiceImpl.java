package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.RoomTypeConverter;
import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.RoomType;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.repository.RoomTypeRepository;
import com.thuc.rooms.service.IRoomTypeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements IRoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final PropertyRepository propertyRepository;
    private final Logger logger = LoggerFactory.getLogger(RoomTypeServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    // Trả về room type mà còn phòng đến tại thời điểm bây giờ
    // Lấy được Property => Query các RoomType có propertyId => Có phòng thuộc loại RoomType mà checkOun< currentDateTime
    @Override
    public List<RoomTypeDto> getAllRoomTypes(String slugProperty) {
        logger.debug("Requested to getAllRoomTypes successfully");
        Property property = propertyRepository.findBySlug(slugProperty);
        if(property == null) {
            throw new ResourceNotFoundException("Property","Slug",slugProperty);
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        StringBuilder sql =new StringBuilder("SELECT * FROM room_type rt WHERE rt.property_id=:propertyId ") ;
        sql.append(" AND EXISTS (SELECT 1 FROM rooms r WHERE rt.id = r.room_type_id AND (r.check_out <:currentDateTime OR r.check_out is null)) ");
        Query query = entityManager.createNativeQuery(sql.toString(), RoomType.class);
        query.setParameter("currentDateTime", currentDateTime);
        query.setParameter("propertyId", property.getId());
        List<RoomType> roomTypes = query.getResultList();
        return roomTypes.stream().map(RoomTypeConverter::toRoomTypDto).toList();

    }
}
