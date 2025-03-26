package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cities")
public class City extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String image;

    @OneToMany(mappedBy = "city")
    private List<Property> properties;

    @OneToMany(mappedBy = "city")
    private List<Trip> trips;
}
