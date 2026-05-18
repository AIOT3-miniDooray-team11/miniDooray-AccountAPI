package com.nhnacadmey.minidoorayaccount.account.service;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;

import java.util.List;

public interface AccountFacade {
    LoginReqAccountResp getAccountByLoginId(String loginId);
    AccountResp getAccountById(long accountId);
    AccountListResp getAccountByIds(List<Long> accountIds);
    void registerAccount(CreateAccountReq req);
    void updateAccount(long accountId, UpdateAccountReq req);
    void deleteAccount(long accountId);

}
