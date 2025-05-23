package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.RefundBillConveter;
import com.thuc.bookings.dto.responseDto.RefundBillDto;
import com.thuc.bookings.entity.RefundBill;
import com.thuc.bookings.repository.RefundBillRepository;
import com.thuc.bookings.service.IRefundBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements IRefundBillService {
    private final RefundBillRepository refundBillRepository;

    @Override
    public void createRefundBill(RefundBillDto refundBillDto) {
        RefundBill refundBill = RefundBillConveter.toRefundBill(refundBillDto);
        refundBillRepository.save(refundBill);
    }
}
