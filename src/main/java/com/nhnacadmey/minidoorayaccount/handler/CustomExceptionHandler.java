package com.nhnacadmey.minidoorayaccount.handler;

import com.nhnacadmey.minidoorayaccount.account.execption.AccountExistException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountInvalidInputException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({
            AccountExistException.class,
            AccountInvalidInputException.class,
            AccountNotExistException.class
    })
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity.badRequest().build();
    }
}
