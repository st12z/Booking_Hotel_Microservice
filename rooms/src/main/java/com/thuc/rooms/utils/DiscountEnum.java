package com.thuc.rooms.utils;

import java.util.ArrayList;
import java.util.List;

public enum DiscountEnum {
    PERCENT("PERCENT"),
    FIXED("FIXED");
    private String type;
    DiscountEnum(String type) {
        this.type = type;
    }
    public List<String> getAllDiscountTypes(){
        List<String> types = new ArrayList<String>();
        for(DiscountEnum e : DiscountEnum.values()){
            types.add(e.type);
        }
        return types;
    }
}
