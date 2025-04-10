package com.thuc.messages.dto;

public record UserDto(String email, String password,
                      String firstName,String lastName, String phoneNumber,
                      String birthDay,String gender, String village,
                      String district, String city, String address) {

}
