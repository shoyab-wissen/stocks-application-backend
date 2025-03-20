package com.stocks.exchange.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_stock")
public class UserStock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stocks stock;

    private int quantity;
    private double averageBuyPrice;
    private double totalInvestment;
    private double currentValue;
    private double profitLoss;
}