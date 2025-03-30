package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {
    private Long total;
    private Integer pageNo;
    private Integer pageSize;
    private T dataPage;
}
