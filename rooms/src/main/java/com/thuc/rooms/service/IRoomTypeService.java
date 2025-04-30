package com.thuc.rooms.service;

import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.Room;
import jakarta.validation.Valid;

import java.util.List;

public interface IRoomTypeService {
    List<RoomTypeDto> getAllRoomTypes(String slugProperty);

    List<RoomTypeDto> getAllRoomTypesBySearch(String slugProperty,SearchDto searchDto);

    Integer checkEnoughRooms(CheckRoomDto checkRoomDto);

    RoomTypeDto getRoomTypeById(Integer id);

    boolean holdRooms(List<CheckRoomDto> roomReversed);

    boolean checkHoldRooms(@Valid BookingDto bookingDto);

    List<Integer> getAvailableRooms(BookingRoomTypeDto bookingRoomTypeDto, int propertyId);

    boolean confirmBooking(@Valid BookingRoomConfirmDto bookingRoomConfirmDto);
}
