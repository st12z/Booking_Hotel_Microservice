package com.thuc.rooms.converter;

import com.thuc.rooms.dto.DiscountCarsDto;
import com.thuc.rooms.entity.DiscountCars;

public class DiscountCarsConverter {
    public static DiscountCarsDto toDiscountCarsDto(DiscountCars discountCars) {
        return DiscountCarsDto.builder()
                .id(discountCars.getId())
                .quantity(discountCars.getQuantity())
                .code(discountCars.getCode())
                .discountValue(discountCars.getDiscountValue())
                .description(discountCars.getDescription())
                .startDate(discountCars.getStartDate())
                .endDate(discountCars.getEndDate())
                .images(discountCars.getImages())
                .isActive(discountCars.getIsActive())
                .build();
    }
}
