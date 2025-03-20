package com.stocks.trading.service;

import com.stocks.trading.dto.TradeRequest;
import com.stocks.trading.dto.TradeResponse;
import com.stocks.trading.exception.InsufficientBalanceException;
import com.stocks.trading.exception.InsufficientSharesException;
import com.stocks.trading.exception.InvalidTradeException;
import com.stocks.trading.models.*;
import com.stocks.trading.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class TradingServiceImpl implements TradingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StocksRepository stocksRepository;

    @Autowired
    private UserStockRepository userStockRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TradeResponse buyStocks(TradeRequest request) throws InsufficientBalanceException, InvalidTradeException {
        if (!validateTrade(request)) {
            throw new InvalidTradeException("Invalid trade parameters");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InvalidTradeException("User not found"));

        Stocks stock = stocksRepository.findBySymbol(request.getStockSymbol())
                .orElseThrow(() -> new InvalidTradeException("Stock not found"));

        double totalCost = request.getQuantity() * request.getPrice();

        if (user.getBalance() < totalCost) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Update user balance
        user.setBalance(user.getBalance() - totalCost);

        // Update or create user stock holding
        UserStock userStock = user.getOwnedStocks().stream()
                .filter(holding -> holding.getStock().getId() == stock.getId())
                .findFirst()
                .orElseGet(() -> {
                    UserStock newHolding = new UserStock();
                    newHolding.setStock(stock);
                    newHolding.setQuantity(0);
                    newHolding.setTotalInvestment(0.0);
                    user.getOwnedStocks().add(newHolding);
                    return newHolding;
                });

        userStock.setQuantity(userStock.getQuantity() + request.getQuantity());
        userStock.setCurrentValue(userStock.getQuantity() * stock.getPrice());
        userStock.setTotalInvestment(userStock.getTotalInvestment() + totalCost);
        userStock.setProfitLoss(userStock.getCurrentValue() - userStock.getTotalInvestment());

        userRepository.save(user);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccountId(String.valueOf(user.getId()));
        transaction.setTransactionType("BUY");
        transaction.setAmount(totalCost);
        transaction.setTransactionDate(Timestamp.valueOf(LocalDateTime.now())); // Fixed: Use Timestamp instead of
                                                                                // String
        transaction.setDescription("Bought " + request.getQuantity() + " shares of " + stock.getSymbol());
        transaction.setStatus("COMPLETED");
        transaction.setStock(stock);
        transaction.setStockQty(request.getQuantity());
        transaction = transactionRepository.save(transaction);

        return TradeResponse.builder()
                .status("SUCCESS")
                .message("Successfully bought " + request.getQuantity() + " shares")
                .totalAmount(totalCost)
                .transactionId(transaction.getTransactionId())
                .updatedBalance(user.getBalance())
                .updatedShareCount(userStock.getQuantity())
                .build();
    }

    @Override
    @Transactional
    public TradeResponse sellStocks(TradeRequest request) throws InsufficientSharesException, InvalidTradeException {
        if (!validateTrade(request)) {
            throw new InvalidTradeException("Invalid trade parameters");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InvalidTradeException("User not found"));

        Stocks stock = stocksRepository.findBySymbol(request.getStockSymbol())
                .orElseThrow(() -> new InvalidTradeException("Stock not found"));

        UserStock userStock = user.getOwnedStocks().stream()
                .filter(holding -> holding.getStock().getId() == stock.getId())
                .findFirst()
                .orElseThrow(() -> new InsufficientSharesException("No shares found to sell"));

        if (userStock.getQuantity() < request.getQuantity()) {
            throw new InsufficientSharesException("Insufficient shares to sell");
        }

        double totalAmount = request.getQuantity() * request.getPrice();

        // Update user balance
        user.setBalance(user.getBalance() + totalAmount);

        // Update user stock holding
        userStock.setQuantity(userStock.getQuantity() - request.getQuantity());
        if (userStock.getQuantity() == 0) {
            user.getOwnedStocks().remove(userStock);
        } else {
            userStock.setCurrentValue(userStock.getQuantity() * stock.getPrice());
            userStock.setProfitLoss(userStock.getCurrentValue() - userStock.getTotalInvestment());
        }

        userRepository.save(user);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccountId(String.valueOf(user.getId()));
        transaction.setTransactionType("SELL");
        transaction.setAmount(totalAmount);
        transaction.setTransactionDate(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setDescription("Sold " + request.getQuantity() + " shares of " + stock.getSymbol());
        transaction.setStatus("COMPLETED");
        transaction.setStock(stock);
        transaction.setStockQty(request.getQuantity());
        transaction = transactionRepository.save(transaction);

        return TradeResponse.builder()
                .status("SUCCESS")
                .message("Successfully sold " + request.getQuantity() + " shares")
                .totalAmount(totalAmount)
                .transactionId(transaction.getTransactionId())
                .updatedBalance(user.getBalance())
                .updatedShareCount(userStock.getQuantity())
                .build();
    }

    @Override
    public double getCurrentStockPrice(String symbol) {
        return stocksRepository.findBySymbol(symbol)
                .map(Stocks::getPrice)
                .orElse(0.0);
    }

    @Override
    public boolean validateTrade(TradeRequest request) {
        return request.getUserId() > 0 &&
                request.getQuantity() > 0 &&
                request.getPrice() > 0 &&
                request.getStockSymbol() != null &&
                !request.getStockSymbol().isEmpty();
    }

    public List<UserStock> getUserHoldings(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getOwnedStocks();
    }

    public Double getTotalPortfolioValue(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getOwnedStocks().stream()
                .mapToDouble(UserStock::getCurrentValue)
                .sum();
    }

    public List<UserStock> getUserPortfolioOrderByValue(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getOwnedStocks().stream()
                .sorted(Comparator.comparing(UserStock::getCurrentValue).reversed())
                .collect(Collectors.toList());
    }
}
