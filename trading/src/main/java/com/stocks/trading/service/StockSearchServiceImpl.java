package com.stocks.trading.service;

import com.stocks.trading.models.Stocks;
import com.stocks.trading.repository.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StockSearchServiceImpl implements StockSearchService {

    @Autowired
    private StocksRepository stocksRepository;

    @Override
    public List<Stocks> searchStocks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllStocks();
        }
        try {
            return stocksRepository.searchStocks(query.trim());
        } catch (Exception e) {
            // Fallback to simple search if fuzzy search fails
            return stocksRepository.simpleSearchStocks(query.trim());
        }
    }

    @Override
    public List<Stocks> getAllStocks() {
        return stocksRepository.findAllByOrderBySymbolAsc();
    }
}
