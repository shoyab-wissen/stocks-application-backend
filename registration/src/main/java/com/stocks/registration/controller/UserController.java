package com.stocks.registration.controller;

import com.stocks.registration.models.ApiResponse;
import com.stocks.registration.models.LoginRequest;
import com.stocks.registration.models.User;
import com.stocks.registration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }
    // GET ALL USERS
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    // DELETE USER
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticateAndGetUser(loginRequest.getEmail(), loginRequest.getPassword());
            if (user != null) {
                // Remove sensitive information before sending
                user.setPassword(null);
                return ResponseEntity.ok(ApiResponse.success(user, "Login successful"));
            } else {
                return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email,
            @RequestParam String dob,      // e.g. "1990-05-15"
            @RequestParam String newPassword) {

        LocalDate dateOfBirth = LocalDate.parse(dob);
        boolean updated = userService.forgotPassword(email, dateOfBirth, newPassword);
        if (updated) {
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.status(404).body("User not found or DOB mismatch");
        }
    }

    // CHANGE PASSWORD
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        boolean changed = userService.changePassword(email, oldPassword, newPassword);
        if (changed) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid email or old password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestParam String email) {
        boolean loggedOut = userService.logoutUser(email);
        if (loggedOut) {
            return ResponseEntity.ok(ApiResponse.success(null, "Logged out successfully"));
        } else {
            return ResponseEntity.status(400)
                .body(ApiResponse.error("Logout failed"));
        }
    }
}
