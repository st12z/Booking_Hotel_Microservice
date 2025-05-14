package com.thuc.rooms.entity;

import com.thuc.rooms.utils.StringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(columnDefinition = "TEXT")
    private String content;

    private float ratingProperty;

    private float ratingStaff;

    private float ratingFacilities;

    private float ratingClean;

    private float ratingComfort;

    private float ratingLocation;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> images;

    private float ratingWifi;
}
