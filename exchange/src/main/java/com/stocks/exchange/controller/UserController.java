package com.stocks.exchange.controller;

import com.stocks.exchange.models.User;
import com.stocks.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin( origins = "*" )
@RequestMapping("/api/exchange/user")
public class UserController {

    @Autowired
    private UserService userService;

    private boolean isAdmin(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    @PostMapping("/addFunds")
    public ResponseEntity<?> addFunds(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam int userId,
            @RequestParam double amount) {

        if (!isAdmin(username, password)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        User updatedUser = userService.addFunds(userId, amount);
        return ResponseEntity.ok("Funds added. New balance = " + updatedUser.getBalance());
    }
}
