package com.stocks.trading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.stocks.trading.config.StockWebSocketHandler;
import com.stocks.trading.models.Stocks;
import com.stocks.trading.repository.StocksRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceUpdateService {
    @Autowired
    private StocksRepository stocksRepository;

    @Autowired
    private StockWebSocketHandler stockWebSocketHandler;

    @Scheduled(cron = "* * * * * *") // Every minute
    public void updatePrices() {
        List<Stocks> allStocks = stocksRepository.findAll();
        for (Stocks stock : allStocks) {
            double currentPrice = stock.getPrice();
            if (stock.getMax() > stock.getMin() && currentPrice > 0) {
                double percentageChange = (Math.random() * 0.02) - 0.01;
                double newPrice = currentPrice * (1 + percentageChange);
                newPrice = Math.max(stock.getMin(), Math.min(stock.getMax(), newPrice));
                System.out.println("newPrice = " + newPrice + " for " + stock.getName());
                stock.setPrice(newPrice);
                stock.setTotalValue(stock.getQuantity() * newPrice);

                stocksRepository.save(stock);

                try {
                    stockWebSocketHandler.broadcastStockUpdate(stock);
                    System.out.println("Broadcasting stock update for " + stock.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
