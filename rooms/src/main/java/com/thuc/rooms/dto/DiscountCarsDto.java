package com.thuc.rooms.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountCarsDto {
    private Integer id;

    private Integer quantity;

    private String code;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String images;

    private Boolean isActive;

    private int discountValue;
}
