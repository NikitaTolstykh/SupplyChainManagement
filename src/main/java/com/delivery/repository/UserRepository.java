package com.delivery.repository;

import com.delivery.entity.User;
import com.delivery.util.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> findAllByRoleIn(List<Role> roles);
    boolean existsByEmail(String email);
}
