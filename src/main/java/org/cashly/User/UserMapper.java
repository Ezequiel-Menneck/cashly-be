package org.cashly.User;

import org.cashly.User.DTOs.CreateUserRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "transactions", expression = "java(new java.util.ArrayList<>())")
    User toEntity(CreateUserRequestDTO createUserRequestDTO);

}
