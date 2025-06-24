package com.thuc.rooms.entity;

import com.thuc.rooms.utils.StringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="room_type")
@Builder
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

    @ManyToMany(mappedBy = "roomTypes")
    private List <Facilities> freeServices;

    private Integer price;

    private Integer maxGuests;



    private Integer area;

    private Integer discount;

    private Integer numBeds;

    private Boolean status;


}
