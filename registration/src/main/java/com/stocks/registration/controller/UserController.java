package com.stocks.registration.controller;

import com.stocks.registration.models.User;
import com.stocks.registration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // REGISTER
    // Expects JSON body with user fields
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User saved = userService.registerUser(user);
        return ResponseEntity.ok(saved);
    }

    // LOGIN
    // Expects JSON { "email": "...", "password": "..." }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        boolean authenticated = userService.authenticate(email, password);
        if (authenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // FORGOT PASSWORD
    // Expects JSON { "email": "...", "dateOfBirth": "yyyy-MM-dd", "newPassword": "..." }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String dobStr = body.get("dateOfBirth");
            String newPassword = body.get("newPassword");

            LocalDate dob = LocalDate.parse(dobStr);

            boolean updated = userService.forgotPassword(email, dob, newPassword);
            if (updated) {
                return ResponseEntity.ok("Password reset successfully");
            } else {
                return ResponseEntity.status(404).body("User not found or DOB mismatch");
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    // CHANGE PASSWORD
    // Expects JSON { "email": "...", "oldPassword": "...", "newPassword": "..." }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        boolean changed = userService.changePassword(email, oldPassword, newPassword);
        if (changed) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid email or old password");
        }
    }
}
