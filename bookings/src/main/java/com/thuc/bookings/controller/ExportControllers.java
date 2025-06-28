package com.thuc.bookings.controller;

import com.thuc.bookings.service.IExportFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportControllers {
    private final Logger log = LoggerFactory.getLogger(ExportControllers.class);
    private final IExportFileService exportFileService;
    @GetMapping("bills")
    public void exportProperties(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=bills.xls");
        response.setContentType("application/octet-stream");
        exportFileService.generateExcelFileOfBills(response);


    }
}
