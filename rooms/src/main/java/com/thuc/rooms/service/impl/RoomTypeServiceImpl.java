package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.RoomTypeConverter;
import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.entity.RoomType;
import com.thuc.rooms.repository.RoomTypeRepository;
import com.thuc.rooms.service.IRoomTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements IRoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final Logger logger = LoggerFactory.getLogger(RoomTypeServiceImpl.class);
    @Override
    public List<RoomTypeDto> getAllRoomTypes(int propertyId) {
        logger.debug("Requested to getAllRoomTypes successfully");
        List<RoomType> roomTypes = roomTypeRepository.findByPropertyId(propertyId);
        return roomTypes.stream().map(RoomTypeConverter::toRoomTypDto).toList();
    }
}
