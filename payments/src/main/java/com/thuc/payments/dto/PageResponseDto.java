package com.thuc.payments.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PageResponseDto<T> implements Serializable {
    private Long total;
    private Integer pageNo;
    private Integer pageSize;
    private T dataPage;
}
