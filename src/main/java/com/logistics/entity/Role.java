package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }

    public enum RoleName {
        ROLE_ADMIN, ROLE_MANAGER, ROLE_WAREHOUSE_MANAGER, ROLE_TRANSPORT_MANAGER, ROLE_EMPLOYEE
    }
}
