package com.thuc.rooms.service;

import jakarta.servlet.http.HttpServletResponse;

public interface IExportFileService {
    void generateExcelOfPropertyRevenue(HttpServletResponse response) ;
}
