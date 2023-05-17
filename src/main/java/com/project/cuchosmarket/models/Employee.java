package com.project.cuchosmarket.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Employee extends User {
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private MarketBranch marketBranch;
}
