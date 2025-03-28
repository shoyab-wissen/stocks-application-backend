package com.stocks.exchange.service;

import com.stocks.exchange.models.Stocks;
import com.stocks.exchange.repositories.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PriceUpdateService {
    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateService.class);

    @Autowired
    private StocksRepository stocksRepository;

    @Scheduled(cron = "0 0/1 * * * *")
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
                if(LocalTime.now() == LocalTime.of(17, 00)){
                    stock.setLastClosingPrice(newPrice);
                }
                stock.setLastUpdated(LocalDateTime.now());
                Map<Date, Double> history = stock.getHistory();
                if (history == null) {
                    history = new HashMap<>();
                }
                
                Date now = Date.from(Instant.now());
                history.put(now, stock.getPrice());
                
                // Log the history size
                logger.info("Stock {} history size: {}", stock.getSymbol(), history.size());
                
                stock.setHistory(history);
                stocksRepository.save(stock);
            }
        }
    }
}
