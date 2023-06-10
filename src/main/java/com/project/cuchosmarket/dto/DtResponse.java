package com.project.cuchosmarket.dto;

import com.project.cuchosmarket.models.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DtResponse extends Customer {
    private boolean error;
    private String message;
    private Object data;
    private String token;
}
