package com.stocks.registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stocks.registration.models.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndDateOfBirth(String email, LocalDate dateOfBirth);

    List<User> findByPanCard(String panCard);    

    List<User> findByAccountNumber(int accountNumber);
}
