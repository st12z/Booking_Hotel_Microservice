package com.thuc.rooms.service;

import com.thuc.rooms.dto.DiscountDto;
import com.thuc.rooms.dto.UserDiscountDto;
import jakarta.validation.constraints.Email;

import java.util.List;

public interface IDiscountService {
    List<DiscountDto> getAllDiscounts();
    UserDiscountDto saveDiscount(UserDiscountDto userDiscountDto);

    List<DiscountDto> getMyDiscountsByEmail(@Email String email);
}
