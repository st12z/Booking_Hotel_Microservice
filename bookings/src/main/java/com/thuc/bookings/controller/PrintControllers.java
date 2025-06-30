package com.thuc.bookings.controller;


import com.thuc.bookings.constants.BookingConstant;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.service.IPrintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/prints")
@RequiredArgsConstructor
public class PrintControllers {
    private final IPrintService printService;
    @GetMapping("bills/{id}")
    public ResponseEntity<byte[]> printBill(@PathVariable Integer id) throws FileNotFoundException {
        printService.exportBill(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("invoice_HD" + id + ".pdf")
                .build());
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(printService.exportBill(id));
    }
}
