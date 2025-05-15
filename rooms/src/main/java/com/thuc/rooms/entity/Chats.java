package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chats extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    private int userSend;

    @ManyToOne
    @JoinColumn(name = "room_chat_id")
    private RoomChats roomChats;

    @ElementCollection
    @CollectionTable(name="chat_images",joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "image_url")
    private List<String> images;
}
