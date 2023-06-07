package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtBranch;
import com.project.cuchosmarket.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query("""
    SELECT new com.project.cuchosmarket.dto.DtBranch(
        mb.id,
        mb.name,
        mb.address
    )
    FROM Branch mb
    """)
    List<DtBranch> findAllSelected();
}
