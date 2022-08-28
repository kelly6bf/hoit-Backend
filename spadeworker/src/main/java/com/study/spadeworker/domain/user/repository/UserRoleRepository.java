package com.study.spadeworker.domain.user.repository;

import com.study.spadeworker.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
