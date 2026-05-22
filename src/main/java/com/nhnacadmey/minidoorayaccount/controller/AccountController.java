package com.nhnacadmey.minidoorayaccount.controller;

import com.nhnacadmey.minidoorayaccount.account.dto.request.AccountListReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.service.AccountFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account-api/v1/accounts")
public class AccountController {
    private final AccountFacade accountFacade;

    @GetMapping("/login")
    public ResponseEntity<LoginReqAccountResp> getAccountByLoginId(@RequestParam("userId") String userId){
        LoginReqAccountResp response = accountFacade.getAccountByLoginId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResp> getAccountById(@PathVariable long id) {
        AccountResp account = accountFacade.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping
    public ResponseEntity<AccountResp> getAccountByUserId(@RequestParam("userId") String userId) {
        AccountResp account = accountFacade.getAccountByUserId(userId);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<AccountListResp> getAccountList(@RequestBody AccountListReq req) {
        AccountListResp response = accountFacade.getAccountByIds(req.accountIdList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerAccount(@RequestBody CreateAccountReq req){
        accountFacade.registerAccount(req);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAccount(@PathVariable long id, @RequestBody UpdateAccountReq req){
        accountFacade.updateAccount(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable long id) {
        accountFacade.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}
