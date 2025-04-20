package com.thuc.rooms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDiscountDto {

    @NotNull(message = "discountId must have value")
    private int discountId;
    @NotBlank(message = "Email not blank")
    private String email;


}
