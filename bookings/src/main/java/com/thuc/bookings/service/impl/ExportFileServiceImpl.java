package com.thuc.bookings.service.impl;

import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.service.IExportFileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ExportFileServiceImpl implements IExportFileService {
    private final BillRepository billRepository;
    @Override
    public void generateExcelFileOfBills(HttpServletResponse response) {
        try{
            List<Bill> bills = billRepository.findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("Bills");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Mã hóa đơn");
            row.createCell(2).setCellValue("Họ và tên");
            row.createCell(3).setCellValue("Email");
            row.createCell(4).setCellValue("Số điện thoại");
            row.createCell(5).setCellValue("Địa chỉ khách hàng");
            row.createCell(6).setCellValue("Tổng tiền gốc");
            row.createCell(7).setCellValue("Giảm giá của hotel");
            row.createCell(8).setCellValue("Giảm giá hotel khi áp mã");
            row.createCell(9).setCellValue("Giảm giá đặt xe khi áp mã");
            row.createCell(10).setCellValue("Tổng tiền sau khi giảm giá");
            row.createCell(11).setCellValue("Trạng thái hóa đơn");
            row.createCell(12).setCellValue("Ngày thanh toán");
            int dataRowIndex = 1;
            for(Bill bill:bills){
                HSSFRow dataRow=sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(bill.getId());
                dataRow.createCell(1).setCellValue(bill.getBillCode());
                dataRow.createCell(2).setCellValue(String.format("%s %s",bill.getFirstName(),bill.getLastName()));
                dataRow.createCell(3).setCellValue(bill.getEmail());
                dataRow.createCell(4).setCellValue(bill.getPhoneNumber());
                dataRow.createCell(5).setCellValue(String.format("%s, %s, %s",bill.getDistrict(),bill.getCity(),bill.getCountry()));
                dataRow.createCell(6).setCellValue(bill.getOriginTotalPayment());
                dataRow.createCell(7).setCellValue(bill.getPricePromotion());
                dataRow.createCell(8).setCellValue(bill.getDiscountHotel());
                dataRow.createCell(9).setCellValue(bill.getDiscountCar());
                dataRow.createCell(10).setCellValue(bill.getNewTotalPayment());
                dataRow.createCell(11).setCellValue(bill.getBillStatus().getValue());
                dataRow.createCell(12).setCellValue(bill.getCreatedAt());
                dataRowIndex++;

            }
            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
