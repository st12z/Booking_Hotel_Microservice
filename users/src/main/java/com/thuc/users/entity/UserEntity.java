package com.thuc.users.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String village;

    private String district;

    private String city;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    private String phoneNumber;

    private LocalDate birthday;

    private String gender;
}
