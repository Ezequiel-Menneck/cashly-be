package org.cashly.User.Transactions;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transactions {

    private String id;

    private BigDecimal amount;

    private String transactionDate;

    private String description;

    private TransactionType type;

    private String categoryId;
}
