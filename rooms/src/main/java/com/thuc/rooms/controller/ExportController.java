package com.thuc.rooms.controller;

import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IExportFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {
    private final Logger log = LoggerFactory.getLogger(ExportController.class);
    private final IExportFileService exportFileService;
    @GetMapping("properties-revenue")
    public void exportProperties(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=properties-revenue.xls");
        response.setContentType("application/octet-stream");
        exportFileService.generateExcelOfPropertyRevenue(response);
    }
    @GetMapping("cities")
    public void exportCities(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=cities.xls");
        response.setContentType("application/octet-stream");
        exportFileService.generateExcelOfCities(response);
    }
    @GetMapping("trips")
    public void exportTrips(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=trips.xls");
        response.setContentType("application/octet-stream");
        exportFileService.generateExcelOfTrips(response);
    }
    @GetMapping("triptypes")
    public void exportTripTypes(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=trip-types.xls");
        response.setContentType("application/octet-stream");
        exportFileService.generateExcelOfTripTypes(response);
    }
}
