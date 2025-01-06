package org.cashly.User;

import org.cashly.User.DTOs.CreateUserRequestDTO;
import org.cashly.User.DTOs.UserDTO;
import org.cashly.User.Transactions.DTOs.*;
import org.cashly.User.Transactions.Transactions;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public UserDTO findUserByIdentifier(@Argument String identifier) {
        return userService.getUserByIdentifier(identifier);
    }

    @QueryMapping
    public List<TransactionsCountByCategoryDTO> getTransactionsCountByCategory(@Argument String identifier) {
        return userService.getTransactionsCountByCategory(identifier);
    }

    @QueryMapping
    public List<TransactionsCountByDateDTO> getTransactionsCountByDate(@Argument String identifier) {
        return userService.getTransactionsCountByDate(identifier);
    }

    @QueryMapping
    public UserBaseSalaryAndSumOfTransactionsAmountDTO getUserBaseSalaryAndSumTransactionsAmount(@Argument String identifier, @Argument Integer month) {
        return userService.getUserBaseSalaryAndSumTransactionsAmount(identifier, month);
    }

    @MutationMapping
    public User createUser(@Argument CreateUserRequestDTO createUserRequest) {
        return userService.createUser(createUserRequest);
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
