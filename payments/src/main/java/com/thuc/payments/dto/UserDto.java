package com.thuc.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String village;

    private String district;

    private String city;

    private String address;

    private String avatar;

    private String phoneNumber;

    private LocalDate birthday;

    private String gender;

    private List<String> roles;
}
