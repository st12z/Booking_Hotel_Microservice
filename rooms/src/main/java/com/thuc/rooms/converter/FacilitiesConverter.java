package com.thuc.rooms.converter;

import com.thuc.rooms.dto.FacilitiesDto;
import com.thuc.rooms.entity.Facilities;

public class FacilitiesConverter {
    public static FacilitiesDto toDto(Facilities facilities) {
        return new FacilitiesDto(facilities.getId(), facilities.getName());
    }
}
