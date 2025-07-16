package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="property_type")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyType extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
