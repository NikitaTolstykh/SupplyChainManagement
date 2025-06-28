package com.delivery.repository;

import com.delivery.entity.User;
import com.delivery.util.OrderStatus;
import com.delivery.util.Role;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> findAllByRoleIn(List<Role> roles);

    boolean existsByEmail(String email);

    List<User> findAllByRole(Role role);

    List<User> findAllByRoleInAndFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            List<Role> roles, String firstName, String email);

    @Query("""
                select u from User u
                where u.role = com.delivery.util.Role.DRIVER
                and u.id not in (
                    select o.driver.id from Order o
                    where o.status in :activeStatuses
                )
            """)
    List<User> findAvailableDrivers(@Param("activeStatuses") List<OrderStatus> activeStatuses);

}
