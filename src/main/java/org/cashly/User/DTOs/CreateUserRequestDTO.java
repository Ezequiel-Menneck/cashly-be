package org.cashly.User.DTOs;

import java.math.BigDecimal;

public record CreateUserRequestDTO(String identifier, BigDecimal baseSalary) {
}
