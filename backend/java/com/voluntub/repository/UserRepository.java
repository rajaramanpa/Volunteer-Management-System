package com.voluntub.repository;

import com.voluntub.entity.Role;
import com.voluntub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);

    boolean existsByEmail(String email);

    long countByRole(Role role);

}
