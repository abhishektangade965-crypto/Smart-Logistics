package com.logistics.serviceimpl;

import com.logistics.dto.UserDto;
import com.logistics.entity.Role;
import com.logistics.entity.User;
import com.logistics.exception.DuplicateResourceException;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.RoleRepository;
import com.logistics.repository.UserRepository;
import com.logistics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return toDto(userRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        return toDto(userRepo.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username)));
    }

    @Override
    public UserDto save(UserDto dto) {
        if (userRepo.existsByUsername(dto.getUsername()))
            throw new DuplicateResourceException("Username already exists: " + dto.getUsername());
        if (userRepo.existsByEmail(dto.getEmail()))
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .department(dto.getDepartment())
                .enabled(true)
                .roles(resolveRoles(dto.getRoleNames()))
                .build();
        return toDto(userRepo.save(user));
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setDepartment(dto.getDepartment());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRoleNames() != null) {
            user.setRoles(resolveRoles(dto.getRoleNames()));
        }
        return toDto(userRepo.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepo.existsById(id)) throw new ResourceNotFoundException("User", "id", id);
        userRepo.deleteById(id);
    }

    @Override
    public void toggleStatus(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id));
        user.setEnabled(!user.isEnabled());
        userRepo.save(user);
    }

    @Override
    public boolean existsByUsername(String username) { return userRepo.existsByUsername(username); }

    @Override
    public boolean existsByEmail(String email) { return userRepo.existsByEmail(email); }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        if (roleNames == null || roleNames.isEmpty()) {
            roleRepo.findByName("ROLE_EMPLOYEE").ifPresent(roles::add);
        } else {
            roleNames.forEach(name -> roleRepo.findByName(name).ifPresent(roles::add));
        }
        return roles;
    }

    private UserDto toDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .phone(u.getPhone())
                .department(u.getDepartment())
                .enabled(u.isEnabled())
                .roleNames(u.getRoles() != null ?
                    u.getRoles().stream().map(Role::getName).collect(Collectors.toSet()) : Set.of())
                .build();
    }
}
