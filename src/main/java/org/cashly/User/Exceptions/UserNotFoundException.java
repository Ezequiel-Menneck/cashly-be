package org.cashly.User.Exceptions;

import org.cashly.Exceptions.BusinessException;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message, String errorCode) {
        super("User of identifier: " + message + " not exists.", errorCode);
    }
}
