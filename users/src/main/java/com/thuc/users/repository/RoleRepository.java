package com.thuc.users.repository;

import com.thuc.users.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByName(String value);

    @Query(" SELECT r FROM RoleEntity r WHERE r.name!=:value")
    List<RoleEntity> findByNotName(String value);
}
