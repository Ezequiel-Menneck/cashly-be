package org.cashly.Category.Exceptions;

import org.cashly.Exceptions.BusinessException;

public class CategoryNotFoundException extends BusinessException {
    public CategoryNotFoundException(String message, String errorCode) {
        super("Category of id: " + message + " not exists.", errorCode);
    }
}
