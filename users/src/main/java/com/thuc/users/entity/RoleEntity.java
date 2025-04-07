package com.thuc.users.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name="roles")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name="permission_id")
    )
    private List<PermissionEntity> permissions;
}
