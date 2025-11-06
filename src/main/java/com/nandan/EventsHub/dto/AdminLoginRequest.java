package com.nandan.EventsHub.dto;

import lombok.Data;

@Data
public class AdminLoginRequest {
    private String adminName;
    private String password;
}
