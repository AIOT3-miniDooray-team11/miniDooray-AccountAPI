package com.nhnacadmey.minidoorayaccount.account.execption;

public class AccountInvalidInputException extends RuntimeException {
    public AccountInvalidInputException(String message) {
        super(message);
    }
}
