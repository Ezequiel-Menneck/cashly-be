package org.cashly.User.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cashly.User.Transactions.DTOs.TransactionsDTO;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;

    private String identifier;

    private String username;

    private BigDecimal baseSalary;

    private List<TransactionsDTO> transactions;

}
