package com.thuc.bookings.controller;

import com.thuc.bookings.constants.RefundBillConstant;
import com.thuc.bookings.dto.requestDto.FilterRefundBillDto;
import com.thuc.bookings.dto.responseDto.*;
import com.thuc.bookings.service.IRefundBillService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/refund-bills")
@RequiredArgsConstructor
public class RefundControllers {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final IRefundBillService refundBillService;
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<RefundBillDto>> getRefundBillById(@PathVariable Integer id){
        logger.debug(" get refund bill by id {}",id);
        SuccessResponseDto<RefundBillDto> response = SuccessResponseDto.<RefundBillDto>builder()
                .code(RefundBillConstant.STATUS_200)
                .message(RefundBillConstant.MESSAGE_200)
                .data(refundBillService.getRefundBillById(id))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/filter")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<RefundBillDto>>>> getAllRefundBills(
            @RequestBody FilterRefundBillDto filterDto
            ) throws ParseException {
        logger.debug(" get all refund bills");
        SuccessResponseDto<PageResponseDto<List<RefundBillDto>>> response = SuccessResponseDto.<PageResponseDto<List<RefundBillDto>>>builder()
                .code(RefundBillConstant.STATUS_200)
                .message(RefundBillConstant.MESSAGE_200)
                .data(refundBillService.getAllRefundBills(filterDto))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<RefundBillDto>>>> search(
            @RequestParam(defaultValue = "1",required = false) Integer pageNo,
            @RequestParam(defaultValue = "10",required = false) Integer pageSize,
            @RequestParam(defaultValue = "") String keyword)
    {
        logger.debug(" search refund bills");
        SuccessResponseDto<PageResponseDto<List<RefundBillDto>>> response = SuccessResponseDto.<PageResponseDto<List<RefundBillDto>>>builder()
                .code(RefundBillConstant.STATUS_200)
                .message(RefundBillConstant.MESSAGE_200)
                .data(refundBillService.getRefundBillsBySearch(keyword,pageNo,pageSize))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/amount-refund-month/{month}")
    public ResponseEntity<SuccessResponseDto<List<StatisticRefundBillByMonth>>> getAmountRefundMonth(@PathVariable Integer month){
        logger.debug(" get amount refund month {}",month);
        SuccessResponseDto<List<StatisticRefundBillByMonth>> response = SuccessResponseDto.<List<StatisticRefundBillByMonth>>builder()
                .code(RefundBillConstant.STATUS_200)
                .message(RefundBillConstant.MESSAGE_200)
                .data(refundBillService.getAmountRefundMonth(month))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
