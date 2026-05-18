package com.nhnacadmey.minidoorayaccount.account.service.impl;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.mapper.AccountMapper;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import com.nhnacadmey.minidoorayaccount.account.service.AccountFacade;
import com.nhnacadmey.minidoorayaccount.account.service.AccountService;
import com.nhnacadmey.minidoorayaccount.deletedaccount.service.DeletedAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AccountFacadeImpl implements AccountFacade {
    private final AccountService accountService;
    private final DeletedAccountService deletedAccountService;

    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginReqAccountResp getAccountByLoginId(String loginId) {
        accountService.isAccountByUserId(loginId);
        LoginAccountProjection projection = accountService.getAccountProjectionByUserId(loginId);

        return accountMapper.toLoginReqAccountResp(projection);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResp getAccountById(long accountId) {
        accountService.isAccountById(accountId);
        Account account = accountService.getAccountById(accountId);

        return accountMapper.toAccountResp(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountListResp getAccountByIds(List<Long> accountIds) {
        List<Account> accounts = accountService.getAccountsByIds(accountIds);
        return accountMapper.toAccountListResp(accounts);
    }

    @Override
    public AccountResp getAccountByUserId(String userId) {
        accountService.isAccountByUserId(userId);
        Account account = accountService.getAccountByUserId(userId);

        return accountMapper.toAccountResp(account);
    }

    @Override
    @Transactional
    public void registerAccount(CreateAccountReq req) {
        accountService.isNotAccountByUserId(req.userId());
        accountService.registerAccount(req);
    }

    @Override
    @Transactional
    public void updateAccount(long accountId, UpdateAccountReq req) {
        accountService.isAccountById(accountId);
        accountService.updateAccount(accountId, req);
    }

    @Override
    @Transactional
    public void deleteAccount(long accountId) {
        accountService.isAccountById(accountId);
        Account account = accountService.getAccountById(accountId);
        deletedAccountService.register(account);
        accountService.deleteAccount(account);
    }
}
