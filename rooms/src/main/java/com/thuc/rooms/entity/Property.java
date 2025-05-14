package com.thuc.rooms.entity;

import com.thuc.rooms.utils.ConvertToSlug;
import com.thuc.rooms.utils.StringConverter;
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
@Table(name="properties")
public class Property extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String propertyType;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(precision = 50, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 50, scale = 10)
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Convert(converter = StringConverter.class)
    @Column(columnDefinition = "jsonb")
    private List <String> facilities;

    @Convert(converter = StringConverter.class)
    @Column(columnDefinition = "jsonb")
    private List <String> images;


    @Column(nullable = true)
    private Double distanceFromCenter;

    @Column(nullable = true)
    private Double distanceFromTrip;

    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "property")
    private List<Room> rooms;

    @OneToMany(mappedBy = "property")
    private List<RoomType> roomTypes;

    @OneToMany(mappedBy = "property")
    private List<Review> reviews;

    private String slug;

    @PrePersist
    public void prePersist() {
        this.slug = ConvertToSlug.convertToSlug(name);
    }


}
