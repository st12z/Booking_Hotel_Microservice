package com.thuc.users.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Email can not be null or empty")
    @Email
    private String email;

    @NotBlank(message = "Password can not be null or empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "firstName can not be null or empty")
    private String firstName;

    @NotBlank(message = "lastName can not be null or empty")
    private String lastName;

    @NotBlank(message = "phoneNumber can not be null or empty")
    @Pattern(regexp = "[0-9]{10}",message = "phoneNumber must be 10 digits")
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;


    private String gender;

    @NotBlank(message = "village can not be null or empty")
    private String village;

    @NotBlank(message = "district can not be null or empty")
    private String district;

    @NotBlank(message = "city can not be null or empty")
    private String city;

    private String address;
}
