package com.thuc.rooms.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityRequestDto implements Serializable {
    @NotEmpty(message = "name is not empty")
    private String name;

    @NotNull(message = "latitudeCenter is not null")
    private BigDecimal latitudeCenter;

    @NotNull(message = "longitudeCenter is not null")
    private BigDecimal longitudeCenter;

}
