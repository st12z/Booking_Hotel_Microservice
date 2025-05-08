package com.thuc.rooms.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name="reviews")
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String content;

    private float ratingProperty;

    private float ratingStaff;

    private float ratingFacilities;

    private float ratingClean;

    private float ratingComfort;

    private float ratingLocation;

    private String image;

    private float ratingWifi;
}
