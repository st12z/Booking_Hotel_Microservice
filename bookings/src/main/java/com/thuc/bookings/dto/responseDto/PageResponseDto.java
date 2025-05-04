package com.thuc.bookings.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> implements Serializable {
    private Long total;
    private Integer pageNo;
    private Integer pageSize;
    private T dataPage;
}
