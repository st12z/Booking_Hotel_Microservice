package com.thuc.BookingService.dto.responseDto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponseDto implements Serializable {
    private int code;
    private String message;
    private Object data;
}
