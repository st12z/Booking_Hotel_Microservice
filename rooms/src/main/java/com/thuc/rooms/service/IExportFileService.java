package com.thuc.rooms.service;

import jakarta.servlet.http.HttpServletResponse;

public interface IExportFileService {
    void generateExcelOfPropertyRevenue(HttpServletResponse response) ;

    void generateExcelOfCities(HttpServletResponse response);

    void generateExcelOfTrips(HttpServletResponse response);

    void generateExcelOfTripTypes(HttpServletResponse response);
}
