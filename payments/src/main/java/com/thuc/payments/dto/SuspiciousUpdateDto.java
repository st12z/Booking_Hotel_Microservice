package com.thuc.payments.dto;

import lombok.Data;

@Data
public class SuspiciousUpdateDto {
    private String uniqueCheck;
    private String billCode;
}
