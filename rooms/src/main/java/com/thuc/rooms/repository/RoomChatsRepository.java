package com.thuc.rooms.repository;

import com.thuc.rooms.entity.RoomChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomChatsRepository extends JpaRepository<RoomChats, Integer> {
    List<RoomChats> findByUserAIdOrUserBId(int userAId, int userBId);


}
