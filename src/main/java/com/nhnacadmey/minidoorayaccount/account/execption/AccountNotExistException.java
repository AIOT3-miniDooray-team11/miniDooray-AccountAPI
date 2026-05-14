package com.nhnacadmey.minidoorayaccount.account.execption;

public class AccountNotExistException extends RuntimeException {
    public AccountNotExistException(String message) {
        super(message);
    }
}
