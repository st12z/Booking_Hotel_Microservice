package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chats,Integer> {
}
