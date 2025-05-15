package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.RoomChatsConverter;
import com.thuc.rooms.dto.RoomChatsDto;
import com.thuc.rooms.entity.RoomChats;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.RoomChatsRepository;
import com.thuc.rooms.service.IRoomChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomChatServiceImpl implements IRoomChatService {
    private final RoomChatsRepository roomChatsRepository;
    @Override
    public List<RoomChatsDto> getRoomChatsOfUser(int userId) {
        List<RoomChats> roomChats = roomChatsRepository.findByUserAIdOrUserBId(userId,userId);
        return roomChats.stream().map(RoomChatsConverter::toRoomChatsDto).collect(Collectors.toList());
    }

    @Override
    public RoomChatsDto createRoomChats(RoomChatsDto roomChatsDto) {
        RoomChats existRoomChats = roomChatsRepository.findByUserAId(roomChatsDto.getUserAId());
        if(existRoomChats != null) {
            throw new ResourceAlreadyExistsException("RoomChats","userAId",String.valueOf(roomChatsDto.getUserAId()));
        }
        RoomChats roomChats = RoomChatsConverter.toRoomChats(roomChatsDto);
        return RoomChatsConverter.toRoomChatsDto(roomChatsRepository.save(roomChats));
    }

    @Override
    public RoomChatsDto getRoomChatsById(int id) {
        RoomChats roomChats = roomChatsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("RoomChats","id",String.valueOf(id)));
        return RoomChatsConverter.toRoomChatsDto(roomChats);
    }
}
