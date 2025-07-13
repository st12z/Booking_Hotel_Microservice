package com.thuc.payments.controller;

import com.thuc.payments.service.IExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/export")
@RestController
@RequiredArgsConstructor
public class ExportControllers {
    private final IExportService exportFileService;
    @GetMapping("/payment-transactions")
    public void exportPaymentTransactions(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=payment-transactions.xls");
        response.setContentType("application/octet-stream");
        exportFileService.generateFileExcelOfPaymentTransactions(response);
    }
    @GetMapping("/suspicious-tran-logs")
    public void exportSuspiciousTranLogs(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=suspicious-tran-logs.xls");
        response.setContentType("application/octet-stream");
        exportFileService.generateFileExcelOfSuspiciousTransactions(response);
    }
}
