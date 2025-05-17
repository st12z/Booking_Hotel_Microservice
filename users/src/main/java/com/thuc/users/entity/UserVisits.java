package com.thuc.users.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user_visits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVisits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    private LocalDateTime accessedAt;
}
