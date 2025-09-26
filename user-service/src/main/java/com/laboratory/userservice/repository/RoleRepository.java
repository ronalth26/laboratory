package com.laboratory.userservice.repository;

import com.laboratory.userservice.model.Role;
import com.laboratory.userservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}