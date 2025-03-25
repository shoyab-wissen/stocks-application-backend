package com.stocks.registration.models;

import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class UserVerif {
    private int id;
    private int userId;
    private int verificationCode;
    private Date expiry;
    private Timestamp createdAt;
}
