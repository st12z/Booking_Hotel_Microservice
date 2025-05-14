package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {

    private int id ;

    private int roomChatId;

    private String content;

    private int userSend;

    private List<String> images;
}
