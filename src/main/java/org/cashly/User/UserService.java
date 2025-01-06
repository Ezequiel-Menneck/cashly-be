package org.cashly.User;

import org.cashly.Category.Category;
import org.cashly.Category.CategoryRepository;
import org.cashly.Category.CategoryService;
import org.cashly.User.Transactions.DTOs.*;
import org.cashly.User.DTOs.CreateUserRequestDTO;
import org.cashly.User.DTOs.UserDTO;
import org.cashly.User.Exceptions.DuplicateUserException;
import org.cashly.User.Exceptions.UserErrorCodeException;
import org.cashly.User.Exceptions.UserNotFoundException;
import org.cashly.User.Transactions.TransactionMapper;
import org.cashly.User.Transactions.Transactions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;


    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public UserService(UserRepository userRepository, CategoryRepository categoryRepository, CategoryService categoryService, UserMapper userMapper, TransactionMapper transactionMapper) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
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

    public UserDTO getUserByIdentifier(String identifier) {
        Optional<User> optionalUser = userRepository.findByIdentifier(identifier);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(identifier, UserErrorCodeException.USER_NOT_FOUND.name());
        }
        User user = optionalUser.get();
        List<TransactionsDTO> transactionsDTO = user.getTransactions().stream()
                .map(transaction -> {
                    TransactionsDTO dto = new TransactionsDTO();
                    BeanUtils.copyProperties(transaction, dto);

                    if (transaction.getCategoryId() != null) {
                        categoryRepository.findById(transaction.getCategoryId()).ifPresent(category -> dto.setCategoryName(category.getName()));
                    }

                    return dto;
                }).toList();

        return userMapper.toUserDTO(user, transactionsDTO);
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
        Optional<Category> optionalCategory = categoryService.findByName(createTransactionDTO.categoryName());

        // optionalCategory is always present
        Transactions transactions = transactionMapper.toEntity(createTransactionDTO, optionalCategory.get().getId());
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
        Optional<Category> optionalCategory = getCategoryIfExists(updateTransactionDTO.categoryName());

        List<Transactions> transactionsList = user.getTransactions();
        transactionsList.stream()
                .filter(t -> t.getId().equals(updateTransactionDTO.transactionId()))
                .findFirst()
                .ifPresent(t -> updateTransactionFields(updateTransactionDTO, t, optionalCategory));
        userRepository.save(user);
        return true;
    }


    public List<TransactionsCountByCategoryDTO> getTransactionsCountByCategory(String identifier) {
        return getUserByIdentifier(identifier).getTransactions().stream()
                .collect(Collectors.groupingBy(TransactionsDTO::getCategoryName, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new TransactionsCountByCategoryDTO(entry.getKey(), entry.getValue().intValue()))
                .toList();
    }

    public List<TransactionsCountByDateDTO> getTransactionsCountByDate(String identifier) {
        int year = LocalDate.now().getYear();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Map<LocalDate, Long> grouped = getUserByIdentifier(identifier).getTransactions().stream()
                .filter(tx -> {
                    LocalDate transactionDate = LocalDate.parse(tx.getTransactionDate(), formatter);
                    return transactionDate.getYear() == year;
                })
                .collect(Collectors.groupingBy(
                        tx -> LocalDate.parse(tx.getTransactionDate(), formatter),
                        Collectors.counting()
                ));

        return grouped.entrySet().stream()
                .map(entry -> new TransactionsCountByDateDTO(entry.getKey().format(outputFormatter), entry.getValue()))
                .sorted(Comparator.comparing(dto -> LocalDate.parse(dto.date(), outputFormatter)))
                .collect(Collectors.toList());
    }

    public UserBaseSalaryAndSumOfTransactionsAmountDTO getUserBaseSalaryAndSumTransactionsAmount(String identifier, Integer month) {
        UserDTO user = getUserByIdentifier(identifier);
        BigDecimal totalAmount = user.getTransactions().stream().filter(tx -> {
            LocalDate transactionDate = LocalDate.parse(tx.getTransactionDate(), formatter);
            return transactionDate.getMonth().getValue() == month;
        }).map(TransactionsDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new UserBaseSalaryAndSumOfTransactionsAmountDTO(user.getBaseSalary(), totalAmount);
    }


    private static void updateTransactionFields(UpdateTransactionDTO updateTransactionDTO, Transactions transaction, Optional<Category> optionalCategory) {
        transaction.setAmount(updateTransactionDTO.amount() != null ? updateTransactionDTO.amount() : transaction.getAmount());
        transaction.setDescription(updateTransactionDTO.description() != null ? updateTransactionDTO.description() : transaction.getDescription());
        transaction.setType(updateTransactionDTO.type() != null ? updateTransactionDTO.type() : transaction.getType());
        transaction.setTransactionDate(updateTransactionDTO.transactionDate() != null ? updateTransactionDTO.transactionDate().toString() : transaction.getTransactionDate());
        optionalCategory.ifPresent(category -> transaction.setCategoryId(category.getId()));
    }

    private User getUser(String identifier) {
        Optional<User> optionalUser = userRepository.findByIdentifier(identifier);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(identifier, UserErrorCodeException.USER_NOT_FOUND.name());
        }
        return optionalUser.get();
    }

    private Optional<Category> getCategoryIfExists(String categoryName) {
        return categoryName != null && !categoryName.isBlank()
                ? categoryService.findByName(categoryName)
                : Optional.empty();
    }

}
