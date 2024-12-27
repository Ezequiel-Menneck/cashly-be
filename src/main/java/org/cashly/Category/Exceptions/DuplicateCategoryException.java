package org.cashly.Category.Exceptions;

import org.cashly.Exceptions.BusinessException;

public class DuplicateCategoryException extends BusinessException {
    public DuplicateCategoryException(String name, String errorCode) {
        super("Category " +  name + " Already exists", errorCode);
    }
}
