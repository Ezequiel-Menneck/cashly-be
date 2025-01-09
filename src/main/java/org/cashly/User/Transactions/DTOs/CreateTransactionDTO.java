package org.cashly.User.Transactions.DTOs;

import org.cashly.User.Transactions.TransactionType;

import java.math.BigDecimal;

public record CreateTransactionDTO(
        String identifier,
        BigDecimal amount,
        String transactionDate,
        String description,
        TransactionType type,
        String categoryName
) { }