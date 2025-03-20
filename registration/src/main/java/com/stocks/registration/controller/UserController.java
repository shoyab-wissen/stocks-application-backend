package com.stocks.registration.controller;

import com.stocks.registration.models.ApiResponse;
import com.stocks.registration.models.User;
import com.stocks.registration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(ApiResponse.success(savedUser, "User registered successfully"));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(
            @RequestParam String email,
            @RequestParam String password) {

        boolean authenticated = userService.authenticate(email, password);
        if (authenticated) {
            return ResponseEntity.ok(ApiResponse.success("Login successful"));
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid credentials"));
        }
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestParam String email,
            @RequestParam String newPassword) {

        boolean updated = userService.forgotPassword(email, newPassword);
        if (updated) {
            return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
        }
    }

    // CHANGE PASSWORD
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        boolean changed = userService.changePassword(email, oldPassword, newPassword);
        if (changed) {
            return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
        } else {
            return ResponseEntity.status(400).body(ApiResponse.error("Invalid email or old password"));
        }
    }
}