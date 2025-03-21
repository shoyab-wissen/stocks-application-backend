package com.stocks.exchange.service;

import com.stocks.exchange.models.Stocks;
import com.stocks.exchange.repositories.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceUpdateService {

    @Autowired
    private StocksRepository stocksRepository;

    @Scheduled(fixedRate = 1000)
    public void updatePrices() {
        List<Stocks> allStocks = stocksRepository.findAll();
        for (Stocks stock : allStocks) {
            double currentPrice = stock.getPrice();

            // If min < max, we can fluctuate
            if (stock.getMax() > stock.getMin() && currentPrice > 0) {
                // random % in [-0.01, 0.01] i.e. ±1%
                double percentageChange = (Math.random() * 0.02) - 0.01;
                double newPrice = currentPrice * (1 + percentageChange);

                // clamp to [min, max]
                if (newPrice < stock.getMin()) {
                    newPrice = stock.getMin();
                } else if (newPrice > stock.getMax()) {
                    newPrice = stock.getMax();
                }

                stock.setPrice(newPrice);
                stock.setTotalValue(stock.getQuantity() * newPrice);
                stock.setLastClosingPrice(newPrice);
                stock.setLastUpdated(LocalDateTime.now());

                stocksRepository.save(stock);
            }
        }
    }
}
