package com.stocks.trading.service;

import com.stocks.trading.dto.TradeRequest;
import com.stocks.trading.dto.TradeResponse;
import com.stocks.trading.exception.InsufficientBalanceException;
import com.stocks.trading.exception.InsufficientSharesException;
import com.stocks.trading.exception.InvalidTradeException;

public interface TradingService {
    TradeResponse buyStocks(TradeRequest request) throws InsufficientBalanceException, InvalidTradeException;
    TradeResponse sellStocks(TradeRequest request) throws InsufficientSharesException, InvalidTradeException;
    double getCurrentStockPrice(String symbol);
    boolean validateTrade(TradeRequest request);
}