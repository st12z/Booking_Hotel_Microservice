package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.DiscountConverter;
import com.thuc.rooms.dto.DiscountDto;
import com.thuc.rooms.entity.Discount;
import com.thuc.rooms.repository.DiscountRepository;
import com.thuc.rooms.service.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements IDiscountService {
    private final DiscountRepository discountRepository;
    @Override
    public List<DiscountDto> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream().map(DiscountConverter::toDiscountDto).toList();
    }
}
