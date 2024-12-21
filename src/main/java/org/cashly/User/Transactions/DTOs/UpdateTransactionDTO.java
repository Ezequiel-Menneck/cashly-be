package org.cashly.User.Transactions.DTOs;

import org.cashly.User.Transactions.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateTransactionDTO(String transactionId, String identifier, BigDecimal amount, LocalDateTime transactionDate, String description, TransactionType type) {
}
