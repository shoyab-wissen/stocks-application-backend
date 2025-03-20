package com.stocks.trading.repository;

import com.stocks.trading.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.balance >= :minBalance")
    List<User> findUsersWithMinBalance(double minBalance);

    @Query("SELECT u FROM User u WHERE u.transactionCount > :count")
    List<User> findActiveTraders(int count);

    List<User> findByAccountNumber(int accountNumber);

    @Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findAllActiveUsers();
}