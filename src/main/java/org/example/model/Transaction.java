package org.example.model;

import jakarta.persistence.*;


public class Transaction {
    public static Long id;

    public Transaction() {
    }

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        Transaction.id = id;
    }
}
