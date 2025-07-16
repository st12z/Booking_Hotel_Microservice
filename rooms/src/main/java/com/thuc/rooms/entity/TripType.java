package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="trip_type")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripType extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String icon;
}
