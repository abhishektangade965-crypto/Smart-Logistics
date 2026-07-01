package com.logistics.service;

import com.logistics.dto.UserDto;
import com.logistics.entity.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(Long id);
    UserDto findByUsername(String username);
    UserDto save(UserDto dto);
    UserDto update(Long id, UserDto dto);
    void delete(Long id);
    void toggleStatus(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
