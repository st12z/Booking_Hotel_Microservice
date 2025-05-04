package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.BillConverter;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.service.IBillService;
import com.thuc.bookings.utils.BillStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements IBillService {
    private final BillRepository billRepository;
    @Override
    public PageResponseDto<List<BillDto>> getMyBills(String email, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Bill> billPage = billRepository.findByUserEmailAndBillStatus(email, BillStatus.SUCCESS,pageable);
        List<BillDto> billDtos = billPage.getContent().stream().map(BillConverter::toBillDto).toList();
        return PageResponseDto.<List<BillDto>>builder()
                .total(billPage.getTotalElements())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .dataPage(billDtos)
                .build();
    }

}