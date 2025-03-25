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

    public User authenticateAndGetUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In production, use proper password hashing
            if (user.getPassword().equals(password)) {
                // Set user as active when they log in
                user.setActive(true);
                return userRepository.save(user);
            }
        }
        return null;
    }

    // Add logout method
    public boolean logoutUser(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Registration logic
    public User registerUser(User user) {
        // In production, add checks for existing email, password hashing, etc.
        return userRepository.save(user);
    }
    // Get All Users
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
    // Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id.intValue());
    }
    

    // Login (simple check by email/password)
    public boolean authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }

    // Forgot Password: only works if email + dateOfBirth match
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

    // Change Password: must match old password
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
