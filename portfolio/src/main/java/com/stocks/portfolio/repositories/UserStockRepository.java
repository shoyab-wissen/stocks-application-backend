package com.stocks.portfolio.repositories;

import com.stocks.portfolio.models.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Integer> {
    List<UserStock> findByStockId(int stockId);
}