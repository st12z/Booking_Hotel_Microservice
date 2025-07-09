package com.thuc.bookings.converter;

import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.utils.BillStatus;

public class BillConverter {
    public static Bill toBill(BookingDto bookingDto, String billCode) {
        Bill bill = Bill.builder()
                .firstName(bookingDto.getFirstName())
                .lastName(bookingDto.getLastName())
                .email(bookingDto.getEmail())
                .phoneNumber(bookingDto.getPhoneNumber())
                .propertyId(bookingDto.getPropertyId())
                .district(bookingDto.getDistrict())
                .city(bookingDto.getCity())
                .country(bookingDto.getCountry())
                .addressDetail(bookingDto.getAddressDetail())
                .originTotalPayment(bookingDto.getOriginTotalPayment())
                .newTotalPayment(bookingDto.getNewTotalPayment())
                .pricePromotion(bookingDto.getPricePromotion())
                .discountHotel(bookingDto.getDiscountHotel())
                .discountCar(bookingDto.getDiscountCar())
                .billStatus(BillStatus.PENDING)
                .isBusinessTrip(bookingDto.getIsBusinessTrip())
                .isShuttleService(bookingDto.getIsShuttleService())
                .bookingForWho(bookingDto.getBookingForWho())
                .specialMessage(bookingDto.getSpecialMessage())
                .userEmail(bookingDto.getUserEmail())
                .billCode(billCode)
                .discountHotelId(bookingDto.getDiscountHotelId())
                .discountCarId(bookingDto.getDiscountCarId())
                .build();
        return bill;
    }

    public static BillDto toBillDto(Bill bill) {
        BillDto billDto = BillDto.builder()
                .id(bill.getId())
                .firstName(bill.getFirstName())
                .lastName(bill.getLastName())
                .email(bill.getEmail())
                .phoneNumber(bill.getPhoneNumber())
                .propertyId(bill.getPropertyId())
                .district(bill.getDistrict())
                .city(bill.getCity())
                .country(bill.getCountry())
                .addressDetail(bill.getAddressDetail())
                .originTotalPayment(bill.getOriginTotalPayment())
                .newTotalPayment(bill.getNewTotalPayment())
                .pricePromotion(bill.getPricePromotion())
                .discountHotel(bill.getDiscountHotel())
                .discountCar(bill.getDiscountCar())
                .billStatus(bill.getBillStatus())
                .isBusinessTrip(bill.getIsBusinessTrip())
                .isShuttleService(bill.getIsShuttleService())
                .bookingForWho(bill.getBookingForWho())
                .specialMessage(bill.getSpecialMessage())
                .userEmail(bill.getUserEmail())
                .billCode(bill.getBillCode())
                .discountHotelId(bill.getDiscountHotelId())
                .discountCarId(bill.getDiscountCarId())
                .createdAt(bill.getCreatedAt())
                .build();
        return billDto;
    }


}
