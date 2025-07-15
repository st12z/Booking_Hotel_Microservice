package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto implements Serializable {
    private Integer id;
    private String name;
    private String image;
    private String slug;
    private LocalDateTime createdAt;
}
