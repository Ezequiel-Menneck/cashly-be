package org.cashly.User.Transactions;

import org.cashly.User.Transactions.DTOs.CreateTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "categoryId", source = "categoryId")
    Transactions toEntity(CreateTransactionDTO createTransactionDTO, String categoryId);

}
