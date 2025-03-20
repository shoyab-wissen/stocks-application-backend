package com.stocks.registration.controller;

import com.stocks.registration.models.ApiResponse;
import com.stocks.registration.models.User;
import com.stocks.registration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody User user) {
        User saved = userService.registerUser(user);
        return ResponseEntity.ok(ApiResponse.success(saved, "User registered successfully"));
    }

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

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<User>> getUser(@RequestParam String email) {
        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(userOpt.get()));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User user) {
        User updated = userService.updateUser(user);
        return ResponseEntity.ok(ApiResponse.success(updated, "User updated successfully"));
    }
}
