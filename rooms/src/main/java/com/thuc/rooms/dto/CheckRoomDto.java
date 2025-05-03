package com.thuc.rooms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckRoomDto {
    @NotNull(message = "roomTypeId is not blank")
    private Integer roomTypeId;

    @NotNull(message = "quantity is not blank")
    private Integer quantity;

    @NotBlank(message = "email is not blank")
    private String email;

    @NotNull(message = "checkIn is not blank")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkIn;

    @NotNull(message = "checkOut is not blank")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOut;

    private Integer propertyId;

}
