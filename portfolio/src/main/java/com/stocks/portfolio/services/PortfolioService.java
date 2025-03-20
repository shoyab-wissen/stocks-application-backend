package com.stocks.portfolio.services;

import com.stocks.portfolio.models.User;
import com.stocks.portfolio.models.UserStock;
import com.stocks.portfolio.models.Stocks;
import com.stocks.portfolio.repositories.UserRepository;
import com.stocks.portfolio.repositories.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PortfolioService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserStockRepository userStockRepository;

    public User getPortfolio(int userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
    }

    public UserStock addStock(int userId, int stockId, int quantity, double buyPrice) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

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

    public User addToWatchlist(int userId, int stockId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
            
        Stocks stock = new Stocks();
        stock.setId(stockId);
        
        user.getWatchlist().add(stock);
        return userRepository.save(user);
    }

    public User removeFromWatchlist(int userId, int stockId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
            
        user.getWatchlist().removeIf(stock -> stock.getId() == stockId);
        return userRepository.save(user);
    }
}