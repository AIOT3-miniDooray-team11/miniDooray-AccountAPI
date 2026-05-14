package com.nhnacadmey.minidoorayaccount.account.service;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;

public interface AccountFacade {
    LoginReqAccountResp getAccountByLoginId(String loginId);
    AccountResp getAccountById(long accountId);
    void registerAccount(CreateAccountReq req);
    void updateAccount(long accountId, UpdateAccountReq req);
    void deleteAccount(long accountId);

}
