package com.cts.user.service;

import com.cts.user.dto.RegisterRequest;
import com.cts.user.dto.UserResponse;
import com.cts.user.model.User;
import com.cts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail().toLowerCase()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail().toLowerCase())
                .phone(req.getPhone())
                .role(req.getRole())
                .build();

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public List<UserResponse> listAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
