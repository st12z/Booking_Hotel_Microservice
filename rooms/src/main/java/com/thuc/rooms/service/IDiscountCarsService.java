package com.thuc.rooms.service;

import com.thuc.rooms.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface IDiscountCarsService {
    List<DiscountCarsDto> getAllDiscountCars();

    List<DiscountCarsDto> getAllMyDiscounts(String email);

    UserDiscountCarDto saveDiscount(UserDiscountCarDto userDiscountCarDto);

    PageResponseDto<List<DiscountCarsDto>> getMyDiscountsByEmailPage(String email, Integer pageNo, Integer pageSize);

    PageResponseDto<List<DiscountCarsDto>> getAllDiscountsByPage(FilterDiscountCarDto filterDto) throws ParseException;

    PageResponseDto<List<DiscountCarsDto>> getSearchDiscountsByPage(String keyword, Integer pageNo, Integer pageSize);

    DiscountCarsDto getDiscountCarId(Integer id);

    DiscountCarsDto updateDiscountCar(Integer id, DiscountCarRequestDto discountCarRequestDto, MultipartFile file);

    DiscountCarsDto createDiscountCar(DiscountCarRequestDto discountCarRequestDto, MultipartFile file);
}
