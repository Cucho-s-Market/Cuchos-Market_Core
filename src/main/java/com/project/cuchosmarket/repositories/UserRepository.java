package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    User findByEmail(String email);



}
