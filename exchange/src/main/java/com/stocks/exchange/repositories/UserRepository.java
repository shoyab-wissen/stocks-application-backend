package com.stocks.exchange.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stocks.exchange.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
