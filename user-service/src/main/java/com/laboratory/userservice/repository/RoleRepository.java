package com.laboratory.userservice.repository;

import com.laboratory.user.service.model.Role;
import com.laboratory.user.service.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}