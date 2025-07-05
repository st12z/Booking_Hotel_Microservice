package com.thuc.bookings.service.impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.thuc.bookings.dto.responseDto.*;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingCars;
import com.thuc.bookings.entity.BookingRooms;
import com.thuc.bookings.entity.RefundBill;
import com.thuc.bookings.exception.ResourceNotFoundException;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.repository.RefundBillRepository;
import com.thuc.bookings.service.IBookingService;
import com.thuc.bookings.service.IPrintService;
import com.thuc.bookings.service.client.PropertiesFeignClient;
import com.thuc.bookings.service.client.RoomTypesFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PrintServiceImpl implements IPrintService {
    private final BillRepository billRepository;
    private final PropertiesFeignClient propertiesFeignClient;
    private final IBookingService bookingService;
    private final RoomTypesFeignClient roomTypesFeignClient;
    private final RefundBillRepository refundBillRepository;
    @Override
    public byte[] exportBill(Integer id)  {
        try {
            Bill bill = billRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Bill","id",String.valueOf(id)));
            ResponseEntity<SuccessResponseDto<PropertyDto>> response = propertiesFeignClient.getPropertyId(bill.getPropertyId());
            PropertyDto propertyDto = response.getBody().getData();
            List<BookingRoomsDto> bookingRooms = bookingService.getListBookingRoomsByBillId(bill.getId());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc);
            PdfFont font = PdfFontFactory.createFont(
                    getClass().getResource("/fonts/arial.ttf").getPath(),
                    PdfEncodings.IDENTITY_H,
                    true
            );
            document.setFont(font);
            // Tiêu đề
            Paragraph header = new Paragraph("THÔNG TIN HÓA ĐƠN - Mã Hóa Đơn: " + bill.getBillCode())
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(header);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            NumberFormat formatMoney = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            // Thông tin khách hàng
            Paragraph customerInfo = new Paragraph(
                    "Thông tin khách hàng:\n" +
                            "Email: " + bill.getEmail() + "\n" +
                            "Địa chỉ: " + bill.getDistrict() + ", " + bill.getCity() + ", " + bill.getCountry() + "\n" +
                            "Họ tên: " + bill.getFirstName() + " " + bill.getLastName() + "\n" +
                            "Tổng tiền: " + formatMoney.format(bill.getOriginTotalPayment()) // dùng helper định dạng tiền
            )
                    .setFontSize(12)
                    .setMarginBottom(15);
            document.add(customerInfo);
            // Thông tin đặt phòng
            Paragraph bookingTitle = new Paragraph("Thông tin đặt phòng:")
                    .setFontSize(13)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                    .setMarginBottom(5);
            document.add(bookingTitle);

            // Bảng thông tin đặt phòng
            Table roomTable = new Table(UnitValue.createPercentArray(new float[]{4, 3, 3, 4, 4, 3, 3}))
                    .useAllAvailableWidth();

            roomTable.addHeaderCell(new Cell().add(new Paragraph("Khách sạn")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Loại phòng")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Số phòng")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Thời gian nhận")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Thời gian trả")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Tổng tiền")).setBold());


            for (BookingRoomsDto item: bookingRooms){
                ResponseEntity<SuccessResponseDto<RoomTypeDto>> responseRoomType = roomTypesFeignClient.getRoomTypeById(item.getRoomTypeId());
                RoomTypeDto roomTypeDto = responseRoomType.getBody().getData();
                roomTable.addCell(new Cell().add(new Paragraph(propertyDto.getName())));
                roomTable.addCell(new Cell().add(new Paragraph(roomTypeDto.getName())));
                roomTable.addCell(new Cell().add(new Paragraph(item.getNumRooms().stream().map(String::valueOf)
                        .collect(Collectors.joining(",")))
                ));
                roomTable.addCell(new Cell().add(new Paragraph(dtf.format(item.getCheckIn()))));
                roomTable.addCell(new Cell().add(new Paragraph(dtf.format(item.getCheckOut()))));
                roomTable.addCell(new Cell().add(new Paragraph(formatMoney.format(item.getNewPayment()))));
            }
            Cell totalLabelCell = new Cell(1, 1)
                    .add(new Paragraph("Tổng tiền").setBold())
                    .setTextAlignment(TextAlignment.LEFT);
            Cell totalAmountCell = new Cell(1, 7)
                    .add(new Paragraph(formatMoney.format(bill.getNewTotalPayment())).setBold())
                    .setTextAlignment(TextAlignment.RIGHT);
            roomTable.addCell(totalLabelCell);
            roomTable.addCell(totalAmountCell);
            document.add(roomTable);
            document.add(new Paragraph("\n"));

            // Thông tin đặt xe
            List<BookingCarsResponseDto> bookingCars =bookingService.getListBookingCarsByBillId(bill.getId());
            if(bookingCars!=null &&!bookingCars.isEmpty()){
                Paragraph carTitle = new Paragraph("Thông tin đặt xe:")
                        .setFontSize(13)
                        .setBold()
                        .setFontColor(ColorConstants.BLUE)
                        .setMarginBottom(5);
                document.add(carTitle);
                Table carTable = new Table(UnitValue.createPercentArray(new float[]{4, 4}))
                        .useAllAvailableWidth();
                carTable.addHeaderCell(new Cell().add(new Paragraph("Loại xe")).setBold());
                carTable.addHeaderCell(new Cell().add(new Paragraph("Tổng tiền thanh toán")).setBold());
                for(BookingCarsResponseDto item: bookingCars){
                    carTable.addCell(new Cell().add(new Paragraph(item.getVehicle().getCarType())));
                    carTable.addCell(new Cell().add(new Paragraph(formatMoney.format(item.getPriceBooking()))));
                }
                document.add(carTable);
            }

            document.close();

            byte[] pdfBytes =baos.toByteArray();
            return pdfBytes;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] exportRefundBills(Integer id) {

        try {
            RefundBill refundBill = refundBillRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("RefundBill","id",String.valueOf(id)));
            Bill bill = billRepository.findByBillCode(refundBill.getVnp_TxnRef());
            if(bill == null){
                throw new ResourceNotFoundException("Bill","billCode",String.valueOf(refundBill.getVnp_TxnRef()));
            }
            ResponseEntity<SuccessResponseDto<PropertyDto>> response = propertiesFeignClient.getPropertyId(bill.getPropertyId());
            PropertyDto propertyDto = response.getBody().getData();
            List<BookingRoomsDto> bookingRooms = bookingService.getListBookingRoomsByBillId(bill.getId());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc);
            PdfFont font = PdfFontFactory.createFont(
                    getClass().getResource("/fonts/arial.ttf").getPath(),
                    PdfEncodings.IDENTITY_H,
                    true
            );
            document.setFont(font);
            // Tiêu đề
            Paragraph header = new Paragraph("THÔNG TIN HÓA ĐƠN HOÀN TIỀN  - Mã Hóa Đơn: " + refundBill.getVnp_TxnRef())
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(header);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            NumberFormat formatMoney = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            // Thông tin khách hàng
            Paragraph customerInfo = new Paragraph(
                    "Thông tin khách hàng:\n" +
                            "Email: " + bill.getEmail() + "\n" +
                            "Địa chỉ: " + bill.getDistrict() + ", " + bill.getCity() + ", " + bill.getCountry() + "\n" +
                            "Họ tên: " + bill.getFirstName() + " " + bill.getLastName() + "\n" +
                            "Tổng tiền: " + formatMoney.format(bill.getOriginTotalPayment()) // dùng helper định dạng tiền
            )
                    .setFontSize(12)
                    .setMarginBottom(15);
            document.add(customerInfo);
            // Thông tin đặt phòng
            Paragraph bookingTitle = new Paragraph("Thông tin đặt phòng:")
                    .setFontSize(13)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                    .setMarginBottom(5);
            document.add(bookingTitle);

            // Bảng thông tin đặt phòng
            Table roomTable = new Table(UnitValue.createPercentArray(new float[]{4, 3, 3, 4, 4, 3, 3}))
                    .useAllAvailableWidth();

            roomTable.addHeaderCell(new Cell().add(new Paragraph("Khách sạn")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Loại phòng")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Số phòng")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Thời gian nhận")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Thời gian trả")).setBold());
            roomTable.addHeaderCell(new Cell().add(new Paragraph("Tổng tiền")).setBold());


            for (BookingRoomsDto item: bookingRooms){
                ResponseEntity<SuccessResponseDto<RoomTypeDto>> responseRoomType = roomTypesFeignClient.getRoomTypeById(item.getRoomTypeId());
                RoomTypeDto roomTypeDto = responseRoomType.getBody().getData();
                roomTable.addCell(new Cell().add(new Paragraph(propertyDto.getName())));
                roomTable.addCell(new Cell().add(new Paragraph(roomTypeDto.getName())));
                roomTable.addCell(new Cell().add(new Paragraph(item.getNumRooms().stream().map(String::valueOf)
                        .collect(Collectors.joining(",")))
                ));
                roomTable.addCell(new Cell().add(new Paragraph(dtf.format(item.getCheckIn()))));
                roomTable.addCell(new Cell().add(new Paragraph(dtf.format(item.getCheckOut()))));
                roomTable.addCell(new Cell().add(new Paragraph(formatMoney.format(item.getNewPayment()))));
            }
            Cell totalLabelCell = new Cell(1, 1)
                    .add(new Paragraph("Tổng tiền").setBold())
                    .setTextAlignment(TextAlignment.LEFT);
            Cell totalAmountCell = new Cell(1, 7)
                    .add(new Paragraph(formatMoney.format(bill.getNewTotalPayment())).setBold())
                    .setTextAlignment(TextAlignment.RIGHT);
            roomTable.addCell(totalLabelCell);
            roomTable.addCell(totalAmountCell);
            document.add(roomTable);
            document.add(new Paragraph("\n"));

            // Thông tin đặt xe
            List<BookingCarsResponseDto> bookingCars =bookingService.getListBookingCarsByBillId(bill.getId());
            if(bookingCars!=null &&!bookingCars.isEmpty()){
                Paragraph carTitle = new Paragraph("Thông tin đặt xe:")
                        .setFontSize(13)
                        .setBold()
                        .setFontColor(ColorConstants.BLUE)
                        .setMarginBottom(5);
                document.add(carTitle);
                Table carTable = new Table(UnitValue.createPercentArray(new float[]{4, 4}))
                        .useAllAvailableWidth();
                carTable.addHeaderCell(new Cell().add(new Paragraph("Loại xe")).setBold());
                carTable.addHeaderCell(new Cell().add(new Paragraph("Tổng tiền thanh toán")).setBold());
                for(BookingCarsResponseDto item: bookingCars){
                    carTable.addCell(new Cell().add(new Paragraph(item.getVehicle().getCarType())));
                    carTable.addCell(new Cell().add(new Paragraph(formatMoney.format(item.getPriceBooking()))));
                }
                document.add(carTable);
            }

            // Thông tin hoàn trả hóa đơn
            Paragraph refundTitle = new Paragraph("Thông tin hoá đơn hoàn trả:")
                    .setFontSize(13)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                    .setMarginBottom(5);
            document.add(refundTitle);
            Table refundBillTable = new Table(UnitValue.createPercentArray(new float[]{3, 3, 4, 4, 4, 4, 4,4}))
                    .useAllAvailableWidth();

            refundBillTable.addHeaderCell(new Cell().add(new Paragraph("Mã ID")).setBold());
            refundBillTable.addHeaderCell(new Cell().add(new Paragraph("Mã hóa đơn")).setBold());
            refundBillTable.addHeaderCell(new Cell().add(new Paragraph("Email hoàn tiền")).setBold());
            refundBillTable.addHeaderCell(new Cell().add(new Paragraph("Loại hoàn tiền")).setBold());
            refundBillTable.addHeaderCell(new Cell().add(new Paragraph("Tổng số tiền đã thanh toán")).setBold());
            refundBillTable.addHeaderCell(new Cell().add(new Paragraph("Tổng số tiền hoàn")).setBold());
            refundBillTable.addHeaderCell(new Cell().add(new Paragraph("Ngày hoàn trả")).setBold());

            refundBillTable.addCell(new Cell().add(new Paragraph(String.valueOf(refundBill.getId()))));
            refundBillTable.addCell(new Cell().add(new Paragraph(refundBill.getVnp_TxnRef())));
            refundBillTable.addCell(new Cell().add(new Paragraph(refundBill.getEmail())));
            refundBillTable.addCell(new Cell().add(new Paragraph(refundBill.getVnp_TransactionType().equals("02") ? "Toàn phần": "Một phần")));
            refundBillTable.addCell(new Cell().add(new Paragraph(formatMoney.format(bill.getNewTotalPayment()))));
            refundBillTable.addCell(new Cell().add(new Paragraph(formatMoney.format(refundBill.getVnp_Amount()))));
            refundBillTable.addCell(new Cell().add(new Paragraph(dtf.format(refundBill.getCreatedAt()))));
            document.add(refundBillTable);
            document.close();

            byte[] pdfBytes =baos.toByteArray();
            return pdfBytes;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
