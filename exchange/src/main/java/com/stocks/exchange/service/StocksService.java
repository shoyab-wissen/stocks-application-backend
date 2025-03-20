package com.stocks.exchange.service;

import com.stocks.exchange.models.Stocks;
import com.stocks.exchange.repositories.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StocksService {

    @Autowired
    private StocksRepository stocksRepository;

    public Stocks addStock(Stocks stock) {
        if (stock.getMax() > stock.getMin()) {
            double initialPrice = stock.getMin() + Math.random() * (stock.getMax() - stock.getMin());
            stock.setPrice(initialPrice);
            stock.setTotalValue(stock.getQuantity() * initialPrice);
        }
        return stocksRepository.save(stock);
    }

    public Stocks updateStock(Stocks updated) {
        Optional<Stocks> opt = stocksRepository.findById(updated.getId());
        if (opt.isPresent()) {
            Stocks existing = opt.get();
            existing.setName(updated.getName());
            existing.setQuantity(updated.getQuantity());
            existing.setMin(updated.getMin());
            existing.setMax(updated.getMax());
            existing.setPrice(updated.getPrice());
            existing.setTotalValue(updated.getTotalValue());
            existing.setSymbol(updated.getSymbol());
            existing.setLastClosingPrice(updated.getLastClosingPrice());
            existing.setLastUpdated(updated.getLastUpdated());
            existing.setHistory(updated.getHistory());
            return stocksRepository.save(existing);
        }
        throw new RuntimeException("Stock not found with id = " + updated.getId());
    }

    public void deleteStock(int id) {
        stocksRepository.deleteById(id);
    }

    public List<Stocks> getAllStocks() {
        return stocksRepository.findAll();
    }
}
