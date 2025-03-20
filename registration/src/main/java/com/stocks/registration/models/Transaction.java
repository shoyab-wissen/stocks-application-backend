package com.stocks.registration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Stocks stock;
    private int stockQty;
}
