package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "room_chats")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomChats extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="user_aid")
    private int userAId;

    @Column(name="user_bid")
    private int userBId;

    @OneToMany(mappedBy = "roomChats")
    private List<Chats> chats;
}
