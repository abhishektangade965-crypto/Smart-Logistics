package com.logistics.dto;

import lombok.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String tokenType;
    private String username;
    private String email;
    private String fullName;
    private Set<String> roles;
}
