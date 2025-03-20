package com.stocks.trading.service;

import com.stocks.trading.models.Stocks;
import java.util.List;

public interface StockSearchService {
    List<Stocks> searchStocks(String query);
    List<Stocks> getAllStocks();
}