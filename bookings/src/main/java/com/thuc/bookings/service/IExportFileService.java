package com.thuc.bookings.service;

import jakarta.servlet.http.HttpServletResponse;

public interface IExportFileService {
    void generateExcelFileOfBills(HttpServletResponse response);
}
