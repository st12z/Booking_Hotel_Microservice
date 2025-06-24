package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name="facilities")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Facilities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String name;

    @ManyToMany
    @JoinTable(name="property_facilities",joinColumns = @JoinColumn(name="facility_id"),
        inverseJoinColumns = @JoinColumn(name="property_id")
    )
    private List<Property> properties;

    @ManyToMany
    @JoinTable(name="roomtypes_facilities",joinColumns = @JoinColumn(name="facility_id"),
        inverseJoinColumns = @JoinColumn(name="room_type_id")
    )
    private List<RoomType> roomTypes;
}
