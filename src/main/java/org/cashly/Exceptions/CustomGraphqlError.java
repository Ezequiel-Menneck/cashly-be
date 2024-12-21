package org.cashly.Exceptions;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;
import java.util.Map;

public class CustomGraphqlError implements GraphQLError {
    private final String message;
    private final String errorCode;

    public CustomGraphqlError(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Map.of("errorCode", errorCode);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return List.of();
    }

    @Override
    public ErrorClassification getErrorType() {
        return null;
    }
}
