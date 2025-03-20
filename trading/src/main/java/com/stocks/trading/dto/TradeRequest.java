package com.stocks.trading.dto;

import lombok.Data;

@Data
public class TradeRequest {
    private int userId;
    private String stockSymbol;
    private int quantity;
    private double price;
    private String tradeType; // BUY or SELL
}