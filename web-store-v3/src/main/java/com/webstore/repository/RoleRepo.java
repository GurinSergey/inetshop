package com.webstore.repository;

import com.webstore.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<UserRole, Integer> {
    UserRole findByRoleName(String name);
}
