package com.stocks.trading.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradeResponse {
    private String status;
    private String message;
    private double totalAmount;
    private int transactionId;
    private double updatedBalance;
    private int updatedShareCount;
}