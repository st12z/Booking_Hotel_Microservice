package com.thuc.rooms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="user_discounts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDiscount extends BaseEntity{
    @Id
    @GeneratedValue
    private Integer id;

    private String email;

    private Integer discountId;

}
