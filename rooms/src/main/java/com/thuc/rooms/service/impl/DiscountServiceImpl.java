package com.thuc.messages.service.impl;


import com.thuc.messages.converter.DiscountConverter;
import com.thuc.messages.dto.DiscountDto;
import com.thuc.messages.entity.Discount;
import com.thuc.messages.repository.DiscountRepository;
import com.thuc.messages.service.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements IDiscountService {
    private final DiscountRepository discountRepository;
    private final Logger log = LoggerFactory.getLogger(DiscountServiceImpl.class);
    @Override
    public List<DiscountDto> getAllDiscounts() {
        log.debug("Request to get all Discounts successfully");
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream().map(DiscountConverter::toDiscountDto).toList();
    }
}
