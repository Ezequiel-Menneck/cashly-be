package org.cashly.User;

import org.cashly.User.DTOs.CreateUserRequestDTO;
import org.cashly.User.Exceptions.DuplicateUserException;
import org.cashly.User.Exceptions.UserErrorCodeException;
import org.cashly.User.Exceptions.UserNotFoundException;
import org.cashly.User.Transactions.DTOs.CreateTransactionDTO;
import org.cashly.User.Transactions.DTOs.UpdateTransactionDTO;
import org.cashly.User.Transactions.TransactionMapper;
import org.cashly.User.Transactions.Transactions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, TransactionMapper transactionMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.transactionMapper = transactionMapper;
    }

    public User createUser(CreateUserRequestDTO createUserRequestDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(createUserRequestDTO.username());
        if (optionalUser.isPresent()) {
            throw new DuplicateUserException(createUserRequestDTO.username(), UserErrorCodeException.DUPLICATED_USER.name());
        }
        User newUser = userMapper.toEntity(createUserRequestDTO);
        userRepository.insert(newUser);
        return newUser;
    }

    public User getUserByIdentifier(String identifier) {
        return getUser(identifier);
    }

    public void deleteUserByIdentifier(String identifier) {
        userRepository.deleteByIdentifier(identifier);
    }

    public Boolean updateUsername(String identifier, String username) {
        User user = getUser(identifier);
        user.setUsername(username);
        userRepository.save(user);
        return true;
    }

    public User updateUserBaseSalary(String identifier, BigDecimal baseSalary) {
        User user = getUser(identifier);
        user.setBaseSalary(baseSalary);
        userRepository.save(user);
        return user;
    }

    public Transactions createTransaction(CreateTransactionDTO createTransactionDTO) {
        User user = getUser(createTransactionDTO.identifier());
        Transactions transactions = transactionMapper.toEntity(createTransactionDTO);
        user.getTransactions().add(transactions);
        userRepository.save(user);
        return transactions;
    }

    public Boolean deleteTransaction(String identifier, String transactionId) {
        User user = getUser(identifier);
        user.getTransactions().removeIf(transaction -> transaction.getId().equals(transactionId));
        userRepository.save(user);
        return true;
    }

    public Boolean updateTransaction(UpdateTransactionDTO updateTransactionDTO) {
        User user = getUser(updateTransactionDTO.identifier());
        List<Transactions> transactionsList = user.getTransactions();
        transactionsList.stream().filter(t -> t.getId().equals(updateTransactionDTO.transactionId())).findFirst().ifPresent(t -> {
            t.setAmount(updateTransactionDTO.amount() != null ? updateTransactionDTO.amount() : t.getAmount());
            t.setDescription(updateTransactionDTO.description() != null ? updateTransactionDTO.description() : t.getDescription());
            t.setType(updateTransactionDTO.type() != null ? updateTransactionDTO.type() : t.getType());
            t.setTransactionDate(updateTransactionDTO.transactionDate() != null ? updateTransactionDTO.transactionDate().toString() : t.getTransactionDate());
        });
        userRepository.save(user);
        return true;
    }

    private User getUser(String identifier) {
        Optional<User> optionalUser = userRepository.findByIdentifier(identifier);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(identifier, UserErrorCodeException.USER_NOT_FOUND.name());
        }
        return optionalUser.get();
    }

}
