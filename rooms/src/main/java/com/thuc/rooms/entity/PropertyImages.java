package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name="property_images")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String image;
}
