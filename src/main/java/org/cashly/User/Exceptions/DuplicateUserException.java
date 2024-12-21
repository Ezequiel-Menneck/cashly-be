package org.cashly.User.Exceptions;

import org.cashly.Exceptions.BusinessException;

public class DuplicateUserException extends BusinessException {
    public DuplicateUserException(String username, String errorCode) {
        super("Username " +  username + " Already exists", errorCode);
    }
}
