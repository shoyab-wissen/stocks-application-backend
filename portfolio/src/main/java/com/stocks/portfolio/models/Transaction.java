package com.stocks.portfolio.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;
    private String accountId;
    private String transactionType;
    private double amount;
    private String transactionDate;
    private String description;
    private String status;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stocks stock;

    private int stockQty;
}
