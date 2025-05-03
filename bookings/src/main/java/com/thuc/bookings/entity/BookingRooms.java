package com.thuc.bookings.entity;

import com.thuc.bookings.utils.StringConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking_rooms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BookingRooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_type_id")
    private int roomTypeId;

    @Column(name = "property_id")
    private int propertyId;

    private int quantityRooms;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "num_rooms", joinColumns = @JoinColumn(name = "booking_room_id"))
    @Column(name = "room_number")
    private List<Integer> numRooms;

    private int billId;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private int dayStays;

    private int originPayment;

    private int promotion;

    private int newPayment;



}
