package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trip extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String tripType;

    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String image;
}
