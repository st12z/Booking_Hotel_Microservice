package com.thuc.bookings.controller;

import com.thuc.bookings.constants.BookingConstant;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.StatisticBillByMonth;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.service.IBillService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillControllers {
    private final Logger log = LoggerFactory.getLogger(BillControllers.class);
    private final IBillService billService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<BillDto>>>> getMyBills(@RequestParam String email,
                                                                                         @RequestParam(defaultValue = "") String keyword,
                                                                                         @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                                                                         @RequestParam(required = false,defaultValue = "10") Integer pageSize
    ){
        log.debug("getMyBills email={}, pageNo={}, pageSize={}", email,pageNo,pageSize);
        SuccessResponseDto<PageResponseDto<List<BillDto>>> response = SuccessResponseDto.<PageResponseDto<List<BillDto>>> builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getMyBills(email,pageNo,pageSize,keyword))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("{billCode}")
    public ResponseEntity<SuccessResponseDto<BillDto>> getBillByBillCode(@PathVariable String billCode){
        log.debug("getBillByCode billCode={}", billCode);
        SuccessResponseDto<BillDto> response = SuccessResponseDto.<BillDto>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getBillByBillCode(billCode))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-bills-today")
    public ResponseEntity<SuccessResponseDto<Integer>> amountBills() {
        log.debug("Request to get properties by  amount bills");
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAmountBillsToday())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-revenue-today")
    public ResponseEntity<SuccessResponseDto<Integer>> getAmountRevenueToDay() {
        log.debug("getAmountRevenueToDay");
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAmountRevenueToday())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-bills-month")
    public ResponseEntity<SuccessResponseDto<List<StatisticBillByMonth>>> getBillsByMonth(@RequestParam Integer month) {
        log.debug("getBillsByMonth month={}", month);
        SuccessResponseDto<List<StatisticBillByMonth>> response = SuccessResponseDto.<List<StatisticBillByMonth>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAmountBillsByMonth(month))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-revenue-month")
    public ResponseEntity<SuccessResponseDto<List<StatisticBillByMonth>>> getRevenueByMonth(@RequestParam Integer month) {
        log.debug("getRevenueMonth month={}", month);
        SuccessResponseDto<List<StatisticBillByMonth>> response = SuccessResponseDto.<List<StatisticBillByMonth>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAmountRevenueByMonth(month))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<SuccessResponseDto<List<BillDto>>> getAllBillsRecently() {
        log.debug("getAllBillsRecently");
        SuccessResponseDto<List<BillDto>> response = SuccessResponseDto.<List<BillDto>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAllBillsRecently())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-bills-property")
    public ResponseEntity<SuccessResponseDto<Map<Integer,Integer>>> getBillByPropertyIds(@RequestParam List<Integer> propertyIds) {
        log.debug("getBillByPropertyId propertyIds={}", propertyIds);
        SuccessResponseDto<Map<Integer,Integer>> response = SuccessResponseDto.<Map<Integer,Integer>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAmountBillsByPropertyIds(propertyIds))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-revenue-property")
    public ResponseEntity<SuccessResponseDto<Map<Integer,Integer>>> getRevenueByPropertyIds(@RequestParam List<Integer> propertyIds) {
        log.debug("getRevenueByPropertyId propertyId={}", propertyIds);
        SuccessResponseDto<Map<Integer,Integer>> response = SuccessResponseDto.<Map<Integer,Integer>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAmountRevenueByPropertyIds(propertyIds))
                .build();
        return ResponseEntity.ok(response);
    }
}