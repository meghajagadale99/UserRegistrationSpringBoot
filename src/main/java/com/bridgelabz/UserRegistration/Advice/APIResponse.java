package com.bridgelabz.UserRegistration.Advice;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class APIResponse {
    private int status;
    private String error;

    public APIResponse() {
        this.status = HttpStatus.OK.value();
        this.error = error;
    }
}