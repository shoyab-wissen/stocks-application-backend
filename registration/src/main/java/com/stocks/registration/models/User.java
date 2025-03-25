package com.stocks.registration.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bank_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean isActive;
    @Column(name = "account_number")
    private int accountNumber;
    private double balance;
    private int transactionCount;
    private String panCard;
    private LocalDate dateOfBirth;
     @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<UserStock> ownedStocks;

    @ManyToMany
    @JoinTable(name = "user_watchlist", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "stock_id"))
    private List<Stocks> watchlist;
}
