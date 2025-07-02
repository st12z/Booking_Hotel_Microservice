package com.thuc.rooms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyRequestDto implements Serializable {

    @NotBlank(message = "name not blank")
    private String name;

    @NotBlank(message = "propertyType not blank")
    private String propertyType;

    @NotBlank(message = "address not blank")
    private String address;

    @NotNull(message = "latitude not null")
    private BigDecimal latitude;

    @NotNull(message = "longitude not null")
    private BigDecimal longitude;

    @NotBlank(message = "overview not blank")
    private String overview;

    @NotEmpty(message = "facilities not empty")
    private List<Integer> facilities;

    @NotEmpty(message = "images not empty")
    private List <MultipartFile> images;

    @NotNull(message = "cityId not null")
    private Integer cityId;
}
