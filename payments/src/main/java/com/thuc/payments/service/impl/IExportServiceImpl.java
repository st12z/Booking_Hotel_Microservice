package com.thuc.payments.service.impl;

import com.thuc.payments.entity.PaymentTransaction;
import com.thuc.payments.entity.SuspiciousPaymentLog;
import com.thuc.payments.repository.PaymentTransactionRepository;
import com.thuc.payments.repository.SuspiciousPaymentLogRepository;
import com.thuc.payments.service.IExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IExportServiceImpl implements IExportService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final SuspiciousPaymentLogRepository suspiciousPaymentLogRepository;

    @Override
    public void generateFileExcelOfPaymentTransactions(HttpServletResponse response) {
        try{
            List<PaymentTransaction> refundBills = paymentTransactionRepository.findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("Payment Transactions");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("VNP_TXN_REF");
            row.createCell(2).setCellValue("VNP_TRANSACTION_NO");
            row.createCell(3).setCellValue("TRANSACTION_TYPE");
            row.createCell(4).setCellValue("VNP_RESPONSE_CODE");
            row.createCell(5).setCellValue("VNP_TRANSACTION_DATE");
            row.createCell(6).setCellValue("VNP_AMOUNT");
            row.createCell(7).setCellValue("CREATED_AT");
            int dataRowIndex = 1;
            for(PaymentTransaction paymentTransaction:refundBills){
                HSSFRow dataRow=sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(paymentTransaction.getId());
                dataRow.createCell(1).setCellValue(paymentTransaction.getVnpTxnRef());
                dataRow.createCell(2).setCellValue(paymentTransaction.getVnpTransactionNo());
                dataRow.createCell(3).setCellValue(paymentTransaction.getTransactionType().getValue());
                dataRow.createCell(4).setCellValue(paymentTransaction.getVnpResponseCode());
                dataRow.createCell(5).setCellValue(paymentTransaction.getVnpTransactionDate());
                dataRow.createCell(6).setCellValue(paymentTransaction.getVnpAmount());
                dataRow.createCell(7).setCellValue(paymentTransaction.getCreatedAt());
                dataRowIndex++;
            }
            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        }catch (Exception e){
            try {
                response.reset(); // xóa content-type trước đó
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"message\": \"Xuất file thất bại: " + e.getMessage().replace("\"", "\\\"") + "\"}");
                response.getWriter().flush();
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Ghi log nội bộ
            }
        }
    }

    @Override
    public void generateFileExcelOfSuspiciousTransactions(HttpServletResponse response) {
        try{
            List<SuspiciousPaymentLog> suspiciousPaymentLogs = suspiciousPaymentLogRepository.findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("Suspicious Payment Logs");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("USER_ID");
            row.createCell(2).setCellValue("AMOUNT");
            row.createCell(3).setCellValue("IP_ADDRESS");
            row.createCell(4).setCellValue("SUSPICIOUS_REASON");
            row.createCell(5).setCellValue("SUSPICIOUS_TYPE");
            row.createCell(6).setCellValue("BILL_CODE");
            row.createCell(7).setCellValue("CREATED_AT");
            int dataRowIndex = 1;
            for(SuspiciousPaymentLog item:suspiciousPaymentLogs){
                HSSFRow dataRow=sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(item.getId());
                dataRow.createCell(1).setCellValue(item.getUserId());
                dataRow.createCell(2).setCellValue(item.getAmount());
                dataRow.createCell(3).setCellValue(item.getIpAddress());
                dataRow.createCell(4).setCellValue(item.getSuspiciousReason());
                dataRow.createCell(5).setCellValue(item.getSuspiciousType().getValue());
                dataRow.createCell(6).setCellValue(item.getBillCode());
                dataRow.createCell(7).setCellValue(item.getCreatedAt());
                dataRowIndex++;
            }
            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        }catch (Exception e){
            try {
                response.reset(); // xóa content-type trước đó
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"message\": \"Xuất file thất bại: " + e.getMessage().replace("\"", "\\\"") + "\"}");
                response.getWriter().flush();
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Ghi log nội bộ
            }
        }
    }
}
