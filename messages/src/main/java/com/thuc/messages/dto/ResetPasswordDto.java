package com.thuc.messages.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String email;
    private String password;
}
