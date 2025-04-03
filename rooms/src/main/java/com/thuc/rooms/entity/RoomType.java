package com.thuc.rooms.entity;

import com.thuc.rooms.utils.StringConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="room_type")
public class RoomType extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;

    private String name;

    @Convert(converter = StringConverter.class)
    @Column(columnDefinition = "jsonb")
    private List<String> freeServices;

    private Integer price;

    private Integer maxGuests;

    private Integer totalRooms;

    private Integer area;

    private Integer discount;

    private Integer numBeds;

    private Boolean status;


}
