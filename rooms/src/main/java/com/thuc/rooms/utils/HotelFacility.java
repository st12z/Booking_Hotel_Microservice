package com.thuc.rooms.utils;

import java.util.ArrayList;
import java.util.List;

public enum HotelFacility {
    FREE_WIFI("Wi-Fi miễn phí"),
    OUTDOOR_POOL("Bể bơi ngoài trời"),
    INDOOR_POOL("Bể bơi trong nhà"),
    GYM("Trung tâm thể dục"),
    RESTAURANT("Nhà hàng"),
    BAR("Quầy bar"),
    ROOM_SERVICE("Dịch vụ phòng 24/7"),
    RECEPTION("Lễ tân 24/7"),
    FREE_PARKING("Bãi đỗ xe miễn phí"),
    AIRPORT_SHUTTLE("Dịch vụ đưa đón sân bay"),
    NON_SMOKING_ROOMS("Phòng không hút thuốc"),
    FAMILY_ROOMS("Phòng gia đình"),
    LAUNDRY_SERVICE("Dịch vụ giặt là"),
    SPA("Spa & chăm sóc sức khỏe"),
    AIR_CONDITIONING("Máy lạnh"),
    SAFETY_DEPOSIT_BOX("Két an toàn"),
    FLAT_SCREEN_TV("Tivi màn hình phẳng"),
    COFFEE_MAKER("Máy pha cà phê"),
    WORK_DESK("Bàn làm việc"),
    PRIVATE_BALCONY("Ban công riêng"),
    SEA_VIEW("View biển"),
    FREE_BREAKFAST("Bữa sáng miễn phí");

    private final String description;
    HotelFacility(String description) {
        this.description = description;
    }
    public List<String> getFacilities() {
        List<String> facilities = new ArrayList<String>();
        for(HotelFacility hotelFacility : HotelFacility.values()) {
            facilities.add(hotelFacility.description);
        }
        return facilities;
    }

}
