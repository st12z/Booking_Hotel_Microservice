package com.thuc.rooms.repository;

import com.thuc.rooms.entity.RoomChats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomChatsRepository extends JpaRepository<RoomChats, Integer> {
    List<RoomChats> findByUserAIdOrUserBId(int userAId, int userBId);


    RoomChats findByUserAId(int userAId);
    @Query("SELECT r FROM RoomChats r WHERE CAST(r.id AS string) LIKE %:keyword%")
    Page<RoomChats> findByIdOrUserId(String keyword, Pageable pageable);
}
