package com.thuc.bookings.entity;

import com.thuc.bookings.utils.BillStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Bill extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String billCode;

    private String firstName;

    private String lastName;

    private String userEmail;

    private String email;

    private String phoneNumber;

    private int propertyId;

    private String district;

    private String city;

    private String country;

    @Column(columnDefinition = "TEXT")
    private String addressDetail;

    private int originTotalPayment;

    private int pricePromotion;

    private int discountHotel;

    private int discountCar;

    private int isBusinessTrip;

    private int isShuttleService;

    private int bookingForWho;

    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;

    @Column(columnDefinition = "TEXT")
    private String specialMessage;

    private int discountHotelId;

    private int discountCarId;

}
