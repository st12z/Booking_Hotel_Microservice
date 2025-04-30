package com.thuc.bookings.functions;

import com.thuc.bookings.converter.BillConverter;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.service.IBookingService;
import com.thuc.bookings.utils.BillStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class BookingFunctions {
    private final Logger log = LoggerFactory.getLogger(BookingFunctions.class);
    private final IBookingService bookingService;
    private final BillRepository billRepository;
    @Bean
    public Function<String, BillDto> sendPayment(){
        return billCode->{
            log.debug("Sending payment for order code: {}", billCode);
            bookingService.updateBillStatus(billCode,BillStatus.SUCCESS);
            Bill bill = billRepository.findByBillCode(billCode);
            bookingService.removeHoldInRedis(billCode);
            return BillConverter.toBllDto(bill);
        };
    }
}
