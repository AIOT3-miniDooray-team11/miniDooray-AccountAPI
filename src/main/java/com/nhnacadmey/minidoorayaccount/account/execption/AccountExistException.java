package com.nhnacadmey.minidoorayaccount.account.execption;

public class AccountExistException extends RuntimeException {
    public AccountExistException(String message) {
        super(message);
    }
}
