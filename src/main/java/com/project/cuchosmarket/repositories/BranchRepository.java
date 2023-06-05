package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtMarketBranch;
import com.project.cuchosmarket.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query("""
    SELECT new com.project.cuchosmarket.dto.DtMarketBranch(
        mb.id,
        mb.name,
        mb.address
    )
    FROM Branch mb
    """)
    List<DtMarketBranch> findAllSelected();
}
