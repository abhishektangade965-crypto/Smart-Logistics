package com.logistics.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDto {
    private Long id;
    @NotBlank private String username;
    @NotBlank @Email private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String department;
    private boolean enabled;
    private Set<String> roleNames;
}
