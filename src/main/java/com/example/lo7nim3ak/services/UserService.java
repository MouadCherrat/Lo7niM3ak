package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.entities.User;
import com.example.lo7nim3ak.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User updateUserRole(Long userId, String newRole) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setRole(newRole);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User registerUser(User user) {
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public void logoutUser(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new RuntimeException("User not found");
        }
        // Optionally, implement session/token invalidation logic if needed.
    }

}
