package com.stocks.portfolio.services;

import com.stocks.portfolio.models.User;
import com.stocks.portfolio.models.UserStock;
import com.stocks.portfolio.models.Stocks;
import com.stocks.portfolio.repositories.UserRepository;
import com.stocks.portfolio.repositories.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStockRepository userStockRepository;

    public User getPortfolio(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return userOpt.get();
    }

    public List<UserStock> getHoldings(int userId) {
        User user = getPortfolio(userId);
        if (user.getOwnedStocks() == null || user.getOwnedStocks().isEmpty()) {
            throw new RuntimeException("No holdings found for user: " + userId);
        }
        return user.getOwnedStocks();
    }

    public List<Stocks> getWatchlist(int userId) {
        try {
            User user = getPortfolio(userId);
            if (user.getWatchlist() == null) {
                user.setWatchlist(new ArrayList<>());
                userRepository.save(user);
            }
            return user.getWatchlist();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public UserStock addStock(int userId, int stockId, int quantity, double buyPrice) {
        User user = getPortfolio(userId);

        // Check if stock already exists in portfolio
        Optional<UserStock> existingStock = user.getOwnedStocks().stream()
                .filter(stock -> stock.getStock().getId() == stockId)
                .findFirst();

        if (existingStock.isPresent()) {
            throw new RuntimeException("Stock already exists in portfolio. Use update endpoint to modify quantity.");
        }

        UserStock userStock = new UserStock();
        Stocks stock = new Stocks();
        stock.setId(stockId);

        userStock.setStock(stock);
        userStock.setQuantity(quantity);
        userStock.setAverageBuyPrice(buyPrice);
        userStock.setTotalInvestment(buyPrice * quantity);
        userStock.setCurrentValue(stock.getPrice() * quantity);
        userStock.setProfitLoss(userStock.getCurrentValue() - userStock.getTotalInvestment());

        user.getOwnedStocks().add(userStock);
        userRepository.save(user);

        return userStock;
    }

    public UserStock updateStock(int userId, int stockId, int quantity, double buyPrice) {
        User user = getPortfolio(userId);
        Optional<UserStock> stockOpt = user.getOwnedStocks().stream()
                .filter(stock -> stock.getStock().getId() == stockId)
                .findFirst();

        if (stockOpt.isEmpty()) {
            throw new RuntimeException("Stock not found in portfolio");
        }

        UserStock userStock = stockOpt.get();
        userStock.setQuantity(quantity);
        userStock.setAverageBuyPrice(buyPrice);
        userStock.setTotalInvestment(buyPrice * quantity);
        userStock.setCurrentValue(userStock.getStock().getPrice() * quantity);
        userStock.setProfitLoss(userStock.getCurrentValue() - userStock.getTotalInvestment());

        userRepository.save(user);
        return userStock;
    }

    public User addToWatchlist(int userId, int stockId) {
        User user = getPortfolio(userId);
        
        // Initialize watchlist if null
        if (user.getWatchlist() == null) {
            user.setWatchlist(new ArrayList<>());
        }

        // Check if stock already exists in watchlist
        boolean exists = user.getWatchlist().stream()
                .anyMatch(stock -> stock.getId() == stockId);

        if (exists) {
            throw new RuntimeException("Stock already exists in watchlist");
        }

        Stocks stock = new Stocks();
        stock.setId(stockId);

        user.getWatchlist().add(stock);
        return userRepository.save(user);
    }

    public User removeFromWatchlist(int userId, int stockId) {
        User user = getPortfolio(userId);

        int initialSize = user.getWatchlist().size();
        user.getWatchlist().removeIf(stock -> stock.getId() == stockId);

        if (user.getWatchlist().size() == initialSize) {
            throw new RuntimeException("Stock not found in watchlist");
        }

        return userRepository.save(user);
    }

    public Map<String, Object> getPortfolioSummary(int userId) {
        User user = getPortfolio(userId);
        List<UserStock> holdings = user.getOwnedStocks();

        if (holdings.isEmpty()) {
            throw new RuntimeException("No holdings found for user: " + userId);
        }

        double totalInvestment = holdings.stream()
                .mapToDouble(UserStock::getTotalInvestment)
                .sum();

        double currentValue = holdings.stream()
                .mapToDouble(UserStock::getCurrentValue)
                .sum();

        double totalProfitLoss = holdings.stream()
                .mapToDouble(UserStock::getProfitLoss)
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalInvestment", totalInvestment);
        summary.put("currentValue", currentValue);
        summary.put("profitLoss", totalProfitLoss);
        summary.put("profitLossPercentage", (totalProfitLoss / totalInvestment) * 100);
        summary.put("numberOfStocks", holdings.size());

        return summary;
    }
}
