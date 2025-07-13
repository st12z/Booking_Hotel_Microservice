package com.thuc.payments.controller;

import com.thuc.payments.constant.PaymentConstant;
import com.thuc.payments.dto.FilterTranLogsDto;
import com.thuc.payments.dto.PageResponseDto;
import com.thuc.payments.dto.SuccessResponseDto;
import com.thuc.payments.dto.SuspiciousTransactionDto;
import com.thuc.payments.service.ISuspiciousTransactionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/suspicious-transaction")
@RequiredArgsConstructor
public class SuspiciousControllers {
    private final ISuspiciousTransactionService suspiciousTransactionService;
    private final Logger logger = LoggerFactory.getLogger(SuspiciousControllers.class);
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<SuspiciousTransactionDto>> getSuspiciousTransactionById(@PathVariable("id") Integer id) {
        logger.debug(" get suspicious transaction by id {}", id);
        SuccessResponseDto<SuspiciousTransactionDto> response = SuccessResponseDto.<SuspiciousTransactionDto>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(suspiciousTransactionService.getSuspiciousTransactionById(id))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-types")
    public ResponseEntity<SuccessResponseDto<List<String>>> getAllSuspiciousTransactionTypes() {
        logger.debug(" get all suspicious transaction type");
        SuccessResponseDto<List<String>> response = SuccessResponseDto.<List<String>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(suspiciousTransactionService.getAllSuspiciousTranTypes())
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/filter")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<SuspiciousTransactionDto>>>> getSuspiciousTransByFilter(@RequestBody FilterTranLogsDto filterDto) throws ParseException {
        logger.debug(" filter tranlogs by filter {}", filterDto);
        SuccessResponseDto<PageResponseDto<List<SuspiciousTransactionDto>>> response = SuccessResponseDto.<PageResponseDto<List<SuspiciousTransactionDto>>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(suspiciousTransactionService.getSuspiciousTransByFilter(filterDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/keyword")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<SuspiciousTransactionDto>>>> getSuspiciousTransByKeyword(
            @RequestParam(defaultValue ="") String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    )  {
        logger.debug("keyword :{}, pageNo:{}, pageSize:{}", keyword, pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<SuspiciousTransactionDto>>> response = SuccessResponseDto.<PageResponseDto<List<SuspiciousTransactionDto>>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(suspiciousTransactionService.getSuspiciousTransByKeyword(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/payment-trans-locked")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<SuspiciousTransactionDto>>>> getSuspiciousTransByLocked(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        logger.debug("get suspicious transaction by locked pageNo {}", pageNo);
        logger.debug("get suspicious transaction by locked pageSize {}", pageSize);
        SuccessResponseDto<PageResponseDto<List<SuspiciousTransactionDto>>> response = SuccessResponseDto.<PageResponseDto<List<SuspiciousTransactionDto>>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(suspiciousTransactionService.getSuspiciousTransLocked(keyword , pageNo, pageSize))
                .build();
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/unclocked")
    public ResponseEntity<SuccessResponseDto<List<Integer>>> unlockSuspiciousTransaction(@RequestBody List<Integer> userIds){
        logger.debug("unlock suspicious transactions by userIds {}", userIds);
        SuccessResponseDto<List<Integer>> response = SuccessResponseDto.<List<Integer>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(suspiciousTransactionService.unClock(userIds))
                .build();
        return ResponseEntity.ok(response);
     }
}
