package com.project.cuchosmarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DtResponse {
    private boolean error;
    private String message;
    private Object data;
    private String token;
}
