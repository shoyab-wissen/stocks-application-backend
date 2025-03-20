package com.stocks.trading.repository;

import com.stocks.trading.models.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Integer> {
    @Query("SELECT us FROM UserStock us WHERE us.profitLoss > 0")
    List<UserStock> findProfitableHoldings();

    @Query("SELECT us FROM UserStock us WHERE us.quantity >= :minQuantity")
    List<UserStock> findLargeHoldings(int minQuantity);
}
