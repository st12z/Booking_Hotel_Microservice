package com.thuc.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto implements Serializable {
    private Integer id;

    private String code;

    private String discountType;

    private Integer discountValue;

    private Integer minBookingAmount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer quantity;

    private String image;
}
