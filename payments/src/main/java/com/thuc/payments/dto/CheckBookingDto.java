package com.thuc.payments.dto;

import com.thuc.payments.utils.SuspiciousTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckBookingDto {
    private Boolean check;
    private SuspiciousTypeEnum suspiciousType;
    private String uniqueCheck;
}
