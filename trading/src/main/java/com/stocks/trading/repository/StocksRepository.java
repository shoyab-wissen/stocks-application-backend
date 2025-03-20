package com.stocks.trading.repository;

import com.stocks.trading.models.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, Integer> {
    @Query("SELECT s FROM Stocks s WHERE s.price BETWEEN :minPrice AND :maxPrice")
    List<Stocks> findStocksInPriceRange(double minPrice, double maxPrice);

    @Query("SELECT s FROM Stocks s WHERE s.price <= :price")
    List<Stocks> findStocksBelowPrice(double price);

    List<Stocks> findBySymbolIn(List<String> symbols);

    @Query("SELECT s FROM Stocks s ORDER BY s.lastUpdated DESC")
    List<Stocks> findRecentlyUpdatedStocks();

    Optional<Stocks> findBySymbol(String symbol);

    // Fuzzy search using PostgreSQL's trigram similarity
    @Query(value = """
            SELECT * FROM stocks WHERE
            CASE
                WHEN EXISTS (
                    SELECT 1 FROM pg_extension WHERE extname = 'pg_trgm'
                ) THEN
                    LOWER(symbol) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    similarity(LOWER(name), LOWER(:query)) > 0.3 OR
                    similarity(LOWER(symbol), LOWER(:query)) > 0.3
                ELSE
                    LOWER(symbol) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    LOWER(name) LIKE LOWER(CONCAT('%', :query, '%'))
            END
            ORDER BY
            CASE
                WHEN LOWER(symbol) = LOWER(:query) THEN 1
                WHEN LOWER(name) = LOWER(:query) THEN 2
                WHEN LOWER(symbol) LIKE LOWER(CONCAT(:query, '%')) THEN 3
                WHEN LOWER(name) LIKE LOWER(CONCAT(:query, '%')) THEN 4
                ELSE 5
            END,
            CASE
                WHEN EXISTS (
                    SELECT 1 FROM pg_extension WHERE extname = 'pg_trgm'
                ) THEN
                    similarity(LOWER(symbol), LOWER(:query)) + similarity(LOWER(name), LOWER(:query))
                ELSE
                    0
            END DESC
            """, nativeQuery = true)
    List<Stocks> searchStocks(String query);

    // Backup simple search method
    @Query("SELECT s FROM Stocks s WHERE " +
            "LOWER(s.symbol) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY s.symbol")
    List<Stocks> simpleSearchStocks(String query);

    // Get all stocks sorted by symbol
    List<Stocks> findAllByOrderBySymbolAsc();
}
