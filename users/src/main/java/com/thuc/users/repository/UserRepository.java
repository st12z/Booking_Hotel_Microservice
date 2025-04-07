package com.thuc.users.repository;

import com.thuc.users.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmail(@NotBlank(message = "Email can not be null or empty") @Email String email);
}
