package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DtIssue {
    private String title;
    private String description;
    private LocalDate creationDate;
    private String user;
    private Long orderId;
}
