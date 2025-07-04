package com.thuc.bookings.dto.requestDto;

import lombok.Data;

import java.util.List;
@Data
public class FilterCarDto {
    List<Integer> choosedStars;
    List<String> choosedCarTypes;
    int choosedPrice;
}
