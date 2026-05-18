package com.nhnacadmey.minidoorayaccount.account.service;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;

import java.util.List;

public interface AccountService {
    void isAccountByUserId(String userId);
    void isNotAccountByUserId(String userId);
    void isAccountById(long accountId);
    LoginAccountProjection getAccountProjectionByUserId(String userId);
    Account getAccountById(long accountId);
    List<Account> getAccountsByIds(List<Long> accountIds);
    void registerAccount(CreateAccountReq req);
    void updateAccount(long accountId, UpdateAccountReq req);
    void deleteAccount(Account account);
}
