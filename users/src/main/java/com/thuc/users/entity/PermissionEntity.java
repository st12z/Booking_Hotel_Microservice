package com.thuc.users.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private List<RoleEntity> roles;
}
