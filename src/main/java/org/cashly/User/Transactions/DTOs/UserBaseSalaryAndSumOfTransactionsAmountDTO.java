package org.cashly.User.Transactions.DTOs;

import java.math.BigDecimal;

public record UserBaseSalaryAndSumOfTransactionsAmountDTO(BigDecimal baseSalary, BigDecimal transactionsAmountSum) {
}
