package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.Item;
import com.project.cuchosmarket.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    @Query("SELECT i FROM Item i WHERE i.product.name = :productName")
    List<Item> findItemsByProduct(@Param("productName") String productName);
}
