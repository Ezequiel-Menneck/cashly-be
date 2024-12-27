package org.cashly.User;

import org.cashly.User.DTOs.CreateUserRequestDTO;
import org.cashly.User.DTOs.UserDTO;
import org.cashly.User.Transactions.DTOs.CreateTransactionDTO;
import org.cashly.User.Transactions.DTOs.UpdateTransactionDTO;
import org.cashly.User.Transactions.Transactions;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    public User createUser(@Argument CreateUserRequestDTO createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @QueryMapping
    public UserDTO findUserByIdentifier(@Argument String identifier) {
        return userService.getUserByIdentifier(identifier);
    }

    @MutationMapping
    public Boolean deleteUserByIdentifier(@Argument String identifier) {
        userService.deleteUserByIdentifier(identifier);
        return true;
    }

    @MutationMapping
    public User updateUserBaseSalary(@Argument String identifier, @Argument BigDecimal baseSalary) {
        return userService.updateUserBaseSalary(identifier, baseSalary);
    }

    @MutationMapping
    public Transactions createTransaction(@Argument CreateTransactionDTO createTransactionDTO) {
        return userService.createTransaction(createTransactionDTO);
    }

    @MutationMapping
    public Boolean deleteTransaction(@Argument String identifier, @Argument String transactionId) {
        return userService.deleteTransaction(identifier, transactionId);
    }

    @MutationMapping Boolean updateTransaction(@Argument UpdateTransactionDTO updateTransactionDTO) {
        return userService.updateTransaction(updateTransactionDTO);
    }

    @MutationMapping
    public Boolean updateUsername(@Argument String identifier, @Argument String username) {
        return userService.updateUsername(identifier, username);
    }
}
