package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.Stock;
import com.project.cuchosmarket.models.StockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, StockId> {
}
