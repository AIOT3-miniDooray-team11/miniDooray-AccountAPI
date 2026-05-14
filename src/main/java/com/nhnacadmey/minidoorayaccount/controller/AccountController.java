package com.nhnacadmey.minidoorayaccount.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account-api/v1/accounts")
public class AccountController {
    @GetMapping
    public void getAccount(){

    }
}
