package com.nhnacadmey.minidoorayaccount.handler;

import com.nhnacadmey.minidoorayaccount.account.execption.AccountExistException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountInvalidInputException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AccountExistException.class)
    public ResponseEntity<Void> handleAccountExist(AccountExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(AccountNotExistException.class)
    public ResponseEntity<Void> handleAccountNotExist(AccountNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AccountInvalidInputException.class)
    public ResponseEntity<Void> handleAccountInvalidInput(AccountInvalidInputException ex) {
        return ResponseEntity.badRequest().build();
    }
}