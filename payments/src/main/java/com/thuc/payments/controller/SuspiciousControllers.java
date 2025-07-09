package com.thuc.payments.controller;

import com.thuc.payments.constant.PaymentConstant;
import com.thuc.payments.dto.SuccessResponseDto;
import com.thuc.payments.dto.SuspiciousTransactionDto;
import com.thuc.payments.service.ISuspiciousTransactionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
