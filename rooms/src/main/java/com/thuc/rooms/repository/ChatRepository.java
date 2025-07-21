package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Chats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chats,Integer> {
    @Query("SELECT c FROM Chats c WHERE c.roomChats.id=:roomChatId AND c.userSend=:userId")
    Page<Chats> findByRoomChatIdAndUserId(int roomChatId, Integer userId, Pageable pageable);

    @Query("SELECT c FROM Chats c WHERE c.roomChats.id=:roomChatId")
    Page<Chats> findByRoomChatId(int roomChatId, Pageable pageable);
}
