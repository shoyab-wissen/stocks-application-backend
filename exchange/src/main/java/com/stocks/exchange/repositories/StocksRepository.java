package com.stocks.exchange.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stocks.exchange.models.Stocks;

public interface StocksRepository extends JpaRepository<Stocks, Integer> {
}
