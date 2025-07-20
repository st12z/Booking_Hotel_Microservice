package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.RoomChatsConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.Chats;
import com.thuc.rooms.entity.RoomChats;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.RoomChatsRepository;
import com.thuc.rooms.service.IRoomChatService;
import com.thuc.rooms.service.client.RolesFeignClient;
import com.thuc.rooms.service.client.UsersFeignClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomChatServiceImpl implements IRoomChatService {
    private final RoomChatsRepository roomChatsRepository;
    private final UsersFeignClient usersFeignClient;
    private final RolesFeignClient rolesFeignClient;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<RoomChatsDto> getRoomChatsOfUser(int userId) {
        List<RoomChats> roomChats = roomChatsRepository.findByUserAIdOrUserBId(userId, userId);

        return roomChats.stream()
                .sorted(Comparator.comparing((RoomChats item) ->
                        item.getChats().stream()
                                .map(Chats::getCreatedAt)
                                .max(Comparator.naturalOrder())
                                .orElse(LocalDateTime.MIN)
                ).reversed()) // sắp xếp giảm dần
                .map(RoomChatsConverter::toRoomChatsDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomChatsDto createRoomChats(RoomChatRequestDto roomChatsDto) {
        SuccessResponseDto<List<RoleDto>> responseRole = rolesFeignClient.getAllRoles().getBody();
        List<RoleDto> roleDtos = responseRole.getData();
        Integer roleId = roleDtos.stream().filter(roleDto -> roleDto.getName().equals("USER")).findFirst().get().getId();
        RoomChats existRoomChats = roomChatsRepository.findByUserAId(roomChatsDto.getUserAId());
        if(existRoomChats != null) {
            throw new ResourceAlreadyExistsException("RoomChats","userAId",String.valueOf(roomChatsDto.getUserAId()));
        }
        StringBuilder builder = new StringBuilder("SELECT u.id FROM users u LEFT JOIN room_chats rc ON u.id=rc.user_bid " +
                " WHERE EXISTS ( SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id !=:roleId) " +
                " GROUP BY u.id " +
                " ORDER BY COUNT(rc.id) ASC " +
                " LIMIT 1 ");
        Query query = entityManager.createNativeQuery(builder.toString(),Integer.class);
        if(roleId!=null){
            query.setParameter("roleId", roleId);
        }
        List<Integer> roleIds = query.getResultList();
        Random rand = new Random();
        int randomIndex = rand.nextInt(roleIds.size());
        RoomChats roomChats = RoomChats.builder()
                .userAId(roomChatsDto.getUserAId())
                .userBId(roleIds.get(randomIndex))
                .build();
        RoomChats savedRoomChats = roomChatsRepository.save(roomChats);
        return RoomChatsConverter.toRoomChatsDto(savedRoomChats);
    }

    @Override
    public RoomChatsDto getRoomChatsById(int id) {
        return RoomChatsConverter.toRoomChatsDto(getRoomChatsEntityById(id));
    }
    private RoomChats getRoomChatsEntityById(Integer id) {
        return roomChatsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("RoomChats","id",String.valueOf(id)));

    }

    @Override
    public PageResponseDto<List<RoomChatsDto>> getAllRoomChats(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoomChats> roomChats = roomChatsRepository.findByIdOrUserId(keyword,pageable);
        return PageResponseDto.<List<RoomChatsDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(roomChats.getTotalElements())
                .dataPage(roomChats.stream().map(RoomChatsConverter::toRoomChatsDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<RoomChatsDto> updateRoomChats(List<RoomChatsDto> roomChatsDto) {
        List<RoomChatsDto> newRoomChats = new ArrayList<>();
        for(RoomChatsDto item:roomChatsDto){
            RoomChats roomChats = getRoomChatsEntityById(item.getId());
            roomChats.setUserBId(item.getUserBId());
            RoomChats updatedRoomChats = roomChatsRepository.save(roomChats);
            newRoomChats.add(RoomChatsConverter.toRoomChatsDto(updatedRoomChats));
        }
        return newRoomChats;
    }


}
