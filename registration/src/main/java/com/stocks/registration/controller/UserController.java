package com.stocks.registration.controller;

import com.stocks.registration.models.ApiResponse;
import com.stocks.registration.models.LoginRequest;
import com.stocks.registration.models.User;
import com.stocks.registration.models.UserDTO;
import com.stocks.registration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        User u = userService.registerUser(user);
        Pattern pattern = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");
        if (u == null) {
            return ResponseEntity.status(400).body("User already exists");
        }
        if (pattern.matcher(user.getPanCard()).matches()) {
            return ResponseEntity.ok("User Registered Successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid PAN");
        }
    }

    // // GET ALL USERS
    // @GetMapping("/users")
    // public ResponseEntity<Iterable<UserDTO>> getAllUsers() {
    //     Iterable<UserDTO> users = userService.getUsers();
    //     return ResponseEntity.ok(users);
    // }

    // GET ALL USERS
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }
    // GET USER BY ID
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // DELETE USER
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // DELETE by Account Number
    @DeleteMapping("/deleteAccount/{accountNumber}")
    public ResponseEntity<String> deleteUserByAccountNumber(@PathVariable int accountNumber) {
        userService.deleteUser(Long.valueOf(accountNumber));
        return ResponseEntity.ok("User deleted successfully");
    }   


    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticateAndGetUser(loginRequest.getEmail(), loginRequest.getPassword());
            if (user != null) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                user.setPassword(null);
                return ResponseEntity.ok(ApiResponse.success(userData, "Login successful"));
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
    // Change here
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email,
            @RequestParam String dob, // e.g. "1990-05-15"
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
