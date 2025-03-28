package com.thuc.rooms.utils;

import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.dto.TripTypeDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public enum TripEnum {
    BEACH(Map.of("Beach","https://static.vecteezy.com/system/resources/previews/002/205/852/original/beach-icon-free-vector.jpg")),
    OUTDOORS(Map.of("Outdoors","https://th.bing.com/th/id/OIP.Sz5Ql26kUfBgxY7EQ9cx6gHaHa?w=1920&h=1920&rs=1&pid=ImgDetMain")),
    MOUNTAIN(Map.of("Mountain","https://th.bing.com/th/id/OIP.zj8ZqZUmyGR4g88LV7tIQAHaHa?rs=1&pid=ImgDetMain"));
    private Map<String,String> trip;
    TripEnum(Map<String,String> trip) {
        this.trip = trip;
    }
    public static List<TripTypeDto> getTrips() {
        List<TripTypeDto> trips = new ArrayList<TripTypeDto>();
        for (TripEnum tripEnum : TripEnum.values()) {
            Map<String,String> tripMap = tripEnum.getTrip();
            for (String key : tripMap.keySet()) {
                trips.add(new TripTypeDto(key,tripMap.get(key)));
            }
        }
        return trips;
    }
}
