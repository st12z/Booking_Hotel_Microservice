package com.thuc.rooms.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponseDto<T> implements Serializable {
    private int code;
    private String message;
    private T data;
}
