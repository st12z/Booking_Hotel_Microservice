package com.thuc.rooms.service;

import com.thuc.rooms.dto.DiscountCarsDto;
import com.thuc.rooms.dto.UserDiscountCarDto;

import java.util.List;

public interface IDiscountCarsService {
    List<DiscountCarsDto> getAllDiscountCars();

    List<DiscountCarsDto> getAllMyDiscounts(String email);

    UserDiscountCarDto saveDiscount(UserDiscountCarDto userDiscountCarDto);
}
