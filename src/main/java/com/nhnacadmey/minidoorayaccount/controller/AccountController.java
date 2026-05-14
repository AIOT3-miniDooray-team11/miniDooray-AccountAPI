package com.nhnacadmey.minidoorayaccount.controller;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.service.AccountFacade;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account-api/v1/accounts")
public class AccountController {
    private final AccountFacade accountFacade;

    @GetMapping
    public ResponseEntity<LoginReqAccountResp> getAccountByUserId(@RequestParam("userId") String userId){
        LoginReqAccountResp response = accountFacade.getAccountByLoginId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<AccountResp> getAccountById(@PathParam("id") long id) {
        AccountResp account = accountFacade.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<Void> registerAccount(CreateAccountReq req){
        accountFacade.registerAccount(req);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateAccount(@PathParam("id") long id, UpdateAccountReq req){
        accountFacade.updateAccount(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccountById(@PathParam("id") long id) {
        accountFacade.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}
