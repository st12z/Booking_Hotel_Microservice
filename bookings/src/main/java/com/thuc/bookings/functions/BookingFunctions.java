package com.thuc.bookings.functions;

import com.thuc.bookings.converter.BillConverter;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.RefundBillDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.repository.RefundBillRepository;
import com.thuc.bookings.service.IBookingService;
import com.thuc.bookings.service.IRefundBillService;
import com.thuc.bookings.utils.BillStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class BookingFunctions {
    private final Logger log = LoggerFactory.getLogger(BookingFunctions.class);
    private final IBookingService bookingService;
    private final BillRepository billRepository;
    private final IRefundBillService refundBillService;
    @Bean
    public Function<String, BillDto> sendPayment(){
        return billCode->{
            log.debug("Sending payment for order code: {}", billCode);
            bookingService.updateBillStatus(billCode,BillStatus.SUCCESS);
            Bill bill = billRepository.findByBillCode(billCode);
            bookingService.removeHoldInRedis(billCode);
            return BillConverter.toBillDto(bill);
        };
    }
    @Bean
    public Function<RefundBillDto, RefundBillDto> sendRefund(){
        return refundBillDto->{
            String billCode= refundBillDto.getVnp_TxnRef();
            log.debug("Sending refund for order code: {}", billCode);
            bookingService.updateBillStatus(billCode,BillStatus.CANCEL);
            refundBillService.createRefundBill(refundBillDto);
            return refundBillDto;
        };
    }
}
