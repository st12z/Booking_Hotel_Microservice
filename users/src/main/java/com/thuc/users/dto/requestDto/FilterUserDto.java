package com.thuc.users.dto.requestDto;

import lombok.Data;

@Data
public class FilterUserDto {
    private Integer pageNo;
    private Integer pageSize;
    private Integer roleId;
    private String sortOption;
    private String timeOption;
    private String startDate;
    private String endDate;
}
