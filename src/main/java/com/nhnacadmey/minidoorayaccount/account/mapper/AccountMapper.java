package com.nhnacadmey.minidoorayaccount.account.mapper;

import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;

import java.util.List;

public interface AccountMapper {
    LoginReqAccountResp toLoginReqAccountResp(LoginAccountProjection projection);
    AccountResp toAccountResp(Account account);
    AccountListResp toAccountListResp(List<Account> accounts);
}
