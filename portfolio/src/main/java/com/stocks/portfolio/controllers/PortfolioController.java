package com.stocks.portfolio.controllers;

import com.stocks.portfolio.models.ApiResponse;
import com.stocks.portfolio.models.User;
import com.stocks.portfolio.models.UserStock;
import com.stocks.portfolio.models.Stocks;
import com.stocks.portfolio.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getPortfolio(@PathVariable int userId) {
        try {
            User user = portfolioService.getPortfolio(userId);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch portfolio: " + e.getMessage()));
        }
    }

    @PostMapping("/{userId}/stocks")
    public ResponseEntity<ApiResponse<UserStock>> addStock(
            @PathVariable int userId,
            @RequestParam int stockId,
            @RequestParam int quantity,
            @RequestParam double buyPrice) {
        try {
            if (quantity <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Quantity must be greater than 0"));
            }
            if (buyPrice <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Buy price must be greater than 0"));
            }

            UserStock userStock = portfolioService.addStock(userId, stockId, quantity, buyPrice);
            return ResponseEntity.ok(ApiResponse.success(userStock,
                    "Stock successfully added to portfolio"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to add stock: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/holdings")
    public ResponseEntity<ApiResponse<List<UserStock>>> getHoldings(@PathVariable int userId) {
        try {
            List<UserStock> holdings = portfolioService.getHoldings(userId);
            return ResponseEntity.ok(ApiResponse.success(holdings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch holdings: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/watchlist")
    public ResponseEntity<ApiResponse<List<Stocks>>> getWatchlist(@PathVariable int userId) {
        try {
            List<Stocks> watchlist = portfolioService.getWatchlist(userId);
            return ResponseEntity.ok(ApiResponse.success(watchlist));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch watchlist: " + e.getMessage()));
        }
    }

    @PostMapping("/{userId}/watchlist/{stockId}")
    public ResponseEntity<ApiResponse<User>> addToWatchlist(
            @PathVariable int userId,
            @PathVariable int stockId) {
        try {
            User user = portfolioService.addToWatchlist(userId, stockId);
            return ResponseEntity.ok(ApiResponse.success(user,
                    "Stock successfully added to watchlist"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to add to watchlist: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/watchlist/{stockId}")
    public ResponseEntity<ApiResponse<User>> removeFromWatchlist(
            @PathVariable int userId,
            @PathVariable int stockId) {
        try {
            User user = portfolioService.removeFromWatchlist(userId, stockId);
            return ResponseEntity.ok(ApiResponse.success(user,
                    "Stock successfully removed from watchlist"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to remove from watchlist: " + e.getMessage()));
        }
    }

    @PutMapping("/{userId}/stocks/{stockId}")
    public ResponseEntity<ApiResponse<UserStock>> updateStockQuantity(
            @PathVariable int userId,
            @PathVariable int stockId,
            @RequestBody Map<String, Object> updates) {
        try {
            int quantity = (int) updates.get("quantity");
            double buyPrice = (double) updates.get("buyPrice");

            if (quantity <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Quantity must be greater than 0"));
            }
            if (buyPrice <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Buy price must be greater than 0"));
            }

            UserStock userStock = portfolioService.updateStock(userId, stockId, quantity, buyPrice);
            return ResponseEntity.ok(ApiResponse.success(userStock,
                    "Stock quantity successfully updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update stock: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPortfolioSummary(@PathVariable int userId) {
        try {
            Map<String, Object> summary = portfolioService.getPortfolioSummary(userId);
            return ResponseEntity.ok(ApiResponse.success(summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch portfolio summary: " + e.getMessage()));
        }
    }
}
