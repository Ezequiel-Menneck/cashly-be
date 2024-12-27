package org.cashly.User.Transactions.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cashly.User.Transactions.TransactionType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsDTO {
    private String id;
    private BigDecimal amount;
    private String transactionDate;
    private String description;
    private TransactionType type;
    private String categoryId;
    private String categoryName;
}

