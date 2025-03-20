package com.stocks.portfolio.controllers;

import com.stocks.portfolio.models.User;
import com.stocks.portfolio.models.UserStock;
import com.stocks.portfolio.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getPortfolio(@PathVariable int userId) {
        User user = portfolioService.getPortfolio(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/stocks")
    public ResponseEntity<UserStock> addStock(
            @PathVariable int userId,
            @RequestParam int stockId,
            @RequestParam int quantity,
            @RequestParam double buyPrice) {
        UserStock userStock = portfolioService.addStock(userId, stockId, quantity, buyPrice);
        return ResponseEntity.ok(userStock);
    }

    @PostMapping("/{userId}/watchlist/{stockId}")
    public ResponseEntity<User> addToWatchlist(
            @PathVariable int userId,
            @PathVariable int stockId) {
        User user = portfolioService.addToWatchlist(userId, stockId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}/watchlist/{stockId}")
    public ResponseEntity<User> removeFromWatchlist(
            @PathVariable int userId,
            @PathVariable int stockId) {
        User user = portfolioService.removeFromWatchlist(userId, stockId);
        return ResponseEntity.ok(user);
    }
}