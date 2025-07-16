package com.thuc.rooms.entity;

import com.thuc.rooms.utils.ConvertToSlug;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="trip")
@Builder
public class Trip extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String tripType;

    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;


    @Column(precision = 50, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 50, scale = 10)
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(unique = true)
    private String slug;

    @PrePersist
    public void prePersist() {
        this.slug = ConvertToSlug.convertToSlug(name);
    }
}
