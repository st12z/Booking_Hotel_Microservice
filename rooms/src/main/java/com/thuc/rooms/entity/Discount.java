package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Discount extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private Integer discountValue;

    private String discountType;

    private Integer minBookingAmount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String image;
}