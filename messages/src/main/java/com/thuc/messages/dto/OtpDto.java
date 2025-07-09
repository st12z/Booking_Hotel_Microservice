package com.thuc.messages.dto;

import lombok.Data;

@Data
public class OtpDto {
    private String email;
    private String uniqueKey;
}
