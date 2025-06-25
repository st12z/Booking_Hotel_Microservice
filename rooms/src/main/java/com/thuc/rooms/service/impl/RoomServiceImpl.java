package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.RoomConverter;
import com.thuc.rooms.dto.RoomDto;
import com.thuc.rooms.dto.RoomRequestDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.Room;
import com.thuc.rooms.entity.RoomType;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.repository.RoomRepository;
import com.thuc.rooms.repository.RoomTypeRepository;
import com.thuc.rooms.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {
    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;
    private final RoomTypeRepository roomTypeRepository;
    @Override
    public RoomDto createRoom(RoomRequestDto roomDto) {
        Room existRoom = roomRepository.findByRoomNumberAndRoomTypeIdAndPropertyId(roomDto.getRoomNumber(),roomDto.getRoomTypeId(),roomDto.getPropertyId());
        if(existRoom!=null){
            throw new ResourceAlreadyExistsException("Room","roomNumber-roomTypeId-getPropertyId",
                    String.format("%d-%d-%d",roomDto.getRoomNumber(),roomDto.getRoomTypeId(),roomDto.getPropertyId()));

        }
        Property property = propertyRepository.findById(roomDto.getPropertyId())
                .orElseThrow(()->new ResourceNotFoundException("Property","propertyId",String.valueOf(roomDto.getPropertyId())));
        RoomType roomType = roomTypeRepository.findById(roomDto.getRoomTypeId())
                .orElseThrow(()->new ResourceNotFoundException("RoomType","roomTypeId",String.valueOf(roomDto.getRoomTypeId())));
        Room room = Room.builder()
                .roomNumber(roomDto.getRoomNumber())
                .roomType(roomType)
                .property(property)
                .status(roomDto.getStatus())
                .build();
        Room savedRoom = roomRepository.save(room);
        return RoomConverter.toRoomDto(savedRoom);
    }

    @Override
    public Integer getQuantityRoomsByPropertyIdAndRoomTypeId(Integer propertyId, Integer roomTypeId) {
        return roomRepository.countByPropertyIdAndRoomTypeId(propertyId,roomTypeId);

    }

    @Override
    public Integer deleteRoom(Integer id) {
        Room existRoom = roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Room","id",String.valueOf(id)));
        roomRepository.deleteById(id);
        return id;
    }
}
