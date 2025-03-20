package com.stocks.portfolio.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Stocks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private double price;
    private int quantity;
    private double totalValue;
    private int min;
    private int max;
    private double minValue;
    private double maxValue;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Date, Double> history;
    private String symbol;
    private double lastClosingPrice;
    private LocalDateTime lastUpdated;
}