package com.thuc.users.repository;

import com.thuc.users.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmail(@NotBlank(message = "Email can not be null or empty") @Email String email);


    @Modifying
    @Query("Update UserEntity u set u.sendEmail=true WHERE u.email= :email")
    void updateSendEmailByEmail(@Param("email") String email);

    @Query(" SELECT u FROM UserEntity u WHERE EXISTS(SELECT r FROM u.roles r WHERE r.id<>:roleId) ")
    List<UserEntity> findByRolesNotContainUser(Integer roleId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.password = :passwordEncode WHERE u.id = :id")
    void updatePasswordById(@Param("id") Integer id, @Param("passwordEncode") String passwordEncode);
}
