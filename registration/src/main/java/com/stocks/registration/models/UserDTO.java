package com.stocks.registration.models;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
    private boolean isActive;
    private int accountNumber;
    private double balance;
    private int transactionCount;
    private String panCard;
    private LocalDate dateOfBirth;
}
