package com.thuc.rooms.service;

import com.thuc.rooms.dto.*;
import jakarta.validation.constraints.Email;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface IDiscountService {
    List<DiscountDto> getAllDiscounts();
    UserDiscountDto saveDiscount(UserDiscountDto userDiscountDto);

    List<DiscountDto> getMyDiscountsByEmail(@Email String email);

    PageResponseDto<List<DiscountDto>> getMyDiscountsByEmailPage(String email, Integer pageNo, Integer pageSize);

    List<String> getAllDiscountTypes();

    PageResponseDto<List<DiscountDto>> getAllDiscountsByFilter(FilterDiscountDto filterDto) throws ParseException;

    PageResponseDto<List<DiscountDto>> getSearchDiscounts(String keyword, Integer pageNo, Integer pageSize);


    DiscountDto getDiscountDtoById(Integer id);

    DiscountDto updateDiscount(Integer id, DiscountHotelRequestDto discountDto, MultipartFile file);

    DiscountDto createDiscount(DiscountHotelRequestDto discountDto, MultipartFile file);
}
