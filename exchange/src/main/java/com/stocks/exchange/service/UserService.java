package com.stocks.exchange.service;

import com.stocks.exchange.models.User;
import com.stocks.exchange.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addFunds(int userId, double amount) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with id = " + userId);
        }
        User user = userOpt.get();
        user.setBalance(user.getBalance() + amount);
        return userRepository.save(user);
    }
}
