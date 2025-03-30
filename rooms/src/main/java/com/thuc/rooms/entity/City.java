package com.thuc.rooms.entity;

import com.thuc.rooms.utils.ConvertToSlug;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String image;

    @OneToMany(mappedBy = "city")
    private List<Property> properties;

    @OneToMany(mappedBy = "city")
    private List<Trip> trips;

    @Column(precision = 50, scale = 10)
    private BigDecimal latitudeCenter;
    @Column(precision = 50, scale = 10)
    private BigDecimal longitudeCenter;

    @Column(unique = true, nullable = false)
    private String slug;

    @PrePersist
    public void prePersist() {
        this.slug = ConvertToSlug.convertToSlug(name);
    }

}
