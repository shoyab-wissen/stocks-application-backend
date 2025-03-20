package com.stocks.registration.services;

import com.stocks.registration.models.User;
import com.stocks.registration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register
    public User registerUser(User user) {
        // Example: check if email already exists
        // In real code, handle password hashing, validations, etc.
        return userRepository.save(user);
    }

    // Login
    public boolean authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }

    // Forgot Password (only if email + dateOfBirth match)
    public boolean forgotPassword(String email, LocalDate dob, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmailAndDateOfBirth(email, dob);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Change Password (must match old password)
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
