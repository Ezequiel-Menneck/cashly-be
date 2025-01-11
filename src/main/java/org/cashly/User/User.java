package org.cashly.User;

import lombok.Data;
import org.cashly.User.Transactions.Transactions;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String identifier;

    private BigDecimal baseSalary;

    private List<Transactions> transactions;
}
