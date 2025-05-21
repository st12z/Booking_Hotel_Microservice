package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterDtoManage {
    private int id;

    private int rateStar;

    private int topBill;

    private int topRevenue;

    private String propertyType;

    private int pageNo=1;

    private int pageSize=10;

    private String keyword;

}
