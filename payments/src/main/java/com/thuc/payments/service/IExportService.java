package com.thuc.payments.service;

import jakarta.servlet.http.HttpServletResponse;

public interface IExportService {
    void generateFileExcelOfPaymentTransactions(HttpServletResponse response);

    void generateFileExcelOfSuspiciousTransactions(HttpServletResponse response);
}
