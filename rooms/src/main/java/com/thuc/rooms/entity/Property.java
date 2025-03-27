package com.thuc.rooms.entity;

import com.thuc.rooms.utils.ConvertToSlug;
import com.thuc.rooms.utils.StringListConverter;
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
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String propertyType;

    private int ratingStar;

    @Column(columnDefinition = "TEXT")
    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT[]")
    private List <String> facilities;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT[]")
    private List <String> images;

    private int numReviews;

    private double avgReviewScore;



    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "property")
    private List<Room> rooms;

    private String slug;

    @PrePersist
    public void prePersist() {
        this.slug = ConvertToSlug.convertToSlug(name);
    }

}
