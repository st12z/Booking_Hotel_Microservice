package com.thuc.messages.dto;

import com.thuc.messages.utils.BillStatus;

public record BillDto(Integer id,String billCode,
                      String firstName,
                      String lastName,
                      String userEmail,
                      String email,
                      String phoneNumber,
                      int propertyId,
                      String district,
                      String city,
                      String country,
                      String addressDetail,
                      int originTotalPayment,
                      int pricePromotion,
                      int discountHotel,
                      int discountCar,
                      int isBusinessTrip,
                      int isShuttleService,
                      int bookingForWho,
                      BillStatus billStatus,
                      String specialMessage,
                      int discountCarId,
                      int discountHotelId
                      ) {
}
