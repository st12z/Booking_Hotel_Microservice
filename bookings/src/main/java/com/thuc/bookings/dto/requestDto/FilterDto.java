package com.thuc.bookings.dto.requestDto;

import lombok.Data;

import java.util.List;
@Data
public class FilterDto {
    List<Integer> choosedStars;
    List<String> choosedCarTypes;
    int choosedPrice;
}
