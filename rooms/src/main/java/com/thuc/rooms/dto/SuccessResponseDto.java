package com.thuc.rooms.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponseDto {
    private int code;
    private String message;
    private Object data;
}
