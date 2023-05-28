package com.project.cuchosmarket.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends User {
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private MarketBranch marketBranch;

    public Employee(String firstName, String lastName, String email, String passwordHash, String passwordSalt, MarketBranch marketBranch) {
        super(firstName, lastName, email, passwordHash, passwordSalt);
        this.marketBranch = marketBranch;
    }
}
