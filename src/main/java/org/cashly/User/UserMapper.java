package org.cashly.User;

import org.cashly.User.DTOs.CreateUserRequestDTO;
import org.cashly.User.DTOs.UserDTO;
import org.cashly.User.Transactions.DTOs.TransactionsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "transactions", expression = "java(new java.util.ArrayList<>())")
    User toEntity(CreateUserRequestDTO createUserRequestDTO);

    @Mapping(target = "transactions", source = "transactionsDTO")
    UserDTO toUserDTO(User user, List<TransactionsDTO> transactionsDTO);

}
