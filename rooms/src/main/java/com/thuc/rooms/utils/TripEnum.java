package com.thuc.rooms.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public enum TripEnum {
    BEACH("BEACH"),
    OUTDOORS("OUTDOORS"),
    MOUNTAIN("MOUNTAIN");
    private String trip;
    TripEnum(String trip) {
        this.trip = trip;
    }
    public List<String> getTrips() {
        List<String> trips = new ArrayList<String>();
        for (TripEnum tripEnum : TripEnum.values()) {
            trips.add(tripEnum.getTrip());
        }
        return trips;
    }
}
