package com.stocks.trading.repository;

import com.stocks.trading.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccountId(String accountId);

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsBetweenDates(String startDate, String endDate);

    List<Transaction> findByTransactionType(String type);

    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.amount >= :amount")
    List<Transaction> findLargeTransactions(String accountId, double amount);

    @Query("SELECT t FROM Transaction t WHERE t.status = :status")
    List<Transaction> findByTransactionStatus(String status);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountId = :accountId AND t.transactionType = :type")
    Double calculateTotalTradeAmount(String accountId, String type);
}