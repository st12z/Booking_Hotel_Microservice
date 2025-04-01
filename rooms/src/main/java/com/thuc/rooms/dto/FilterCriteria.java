package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class FilterCriteria {
    private String key;
    private String operation;
    private Object value;
}
