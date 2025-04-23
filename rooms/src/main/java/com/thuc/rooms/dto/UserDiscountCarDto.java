package com.thuc.rooms.dto;

import jakarta.validation.constraints.Email;
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
public class UserDiscountCarDto {

    @NotNull(message = "discountCarId not be null")
    private int discountCarId;

    @NotBlank(message = "Email not be blank")
    @Email
    private String email;
}
