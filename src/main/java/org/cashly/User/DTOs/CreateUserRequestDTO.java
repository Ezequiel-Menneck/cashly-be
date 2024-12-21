package org.cashly.User.DTOs;

import java.math.BigDecimal;

public record CreateUserRequestDTO(String username, String identifier, BigDecimal baseSalary) {
}
