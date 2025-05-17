package com.thuc.bookings.controller;

import com.thuc.bookings.constants.BookingConstant;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.service.IBillService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillControllers {
    private final Logger log = LoggerFactory.getLogger(BillControllers.class);
    private final IBillService billService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<BillDto>>>> getMyBills(@RequestParam String email,
                                                                                         @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                                                                         @RequestParam(required = false,defaultValue = "10") Integer pageSize
    ){
        log.debug("getMyBills email={}, pageNo={}, pageSize={}", email,pageNo,pageSize);
        SuccessResponseDto<PageResponseDto<List<BillDto>>> response = SuccessResponseDto.<PageResponseDto<List<BillDto>>> builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getMyBills(email,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<BillDto>>>> getBillsByKeyword(@RequestParam String email,
                                                                                                @RequestParam(required = false,defaultValue = "") String keyword,
                                                                                                @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                                                                                @RequestParam(required = false,defaultValue = "5") Integer pageSize
                                                                                                ){
        log.debug("getBillsByKeyword keyword={}", keyword);
        SuccessResponseDto<PageResponseDto<List<BillDto>>> response = SuccessResponseDto.<PageResponseDto<List<BillDto>>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getBillsByKeyword(email,keyword,pageNo,pageSize))
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
    @GetMapping("/amount-bills")
    public ResponseEntity<SuccessResponseDto<Integer>> amountProperties() {
        log.debug("Request to get properties by  amount bills");
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(billService.getAmountBills())
                .build();
        return ResponseEntity.ok(response);
    }
}
