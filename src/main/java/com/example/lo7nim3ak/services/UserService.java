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

}
