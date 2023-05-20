package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
}
