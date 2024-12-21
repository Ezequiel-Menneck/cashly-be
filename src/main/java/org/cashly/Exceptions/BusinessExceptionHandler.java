package org.cashly.Exceptions;

import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class BusinessExceptionHandler {

    @GraphQlExceptionHandler(BusinessException.class)
    public GraphQLError handleBusinessException(BusinessException ex) {
        return new CustomGraphqlError(ex.getMessage(), ex.getErrorCode());
    }
}
