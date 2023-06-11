package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Boolean existsByDni(long dni);
    Optional<DtCustomer> findByEmail(String email);
}
