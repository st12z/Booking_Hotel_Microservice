package com.thuc.rooms.converter;

import com.thuc.rooms.dto.DiscountDto;
import com.thuc.rooms.entity.Discount;

public class DiscountConverter {
    public static DiscountDto toDiscountDto(Discount discount) {
        return DiscountDto.builder()
                .id(discount.getId())
                .code(discount.getCode())
                .discountType(discount.getDiscountType())
                .minBookingAmount(discount.getMinBookingAmount())
                .image(discount.getImage())
                .discountValue(discount.getDiscountValue())
                .endDate(discount.getEndDate())
                .startDate(discount.getStartDate())
                .quantity(discount.getQuantity())
                .build();
    }
}