package com.nhnacadmey.minidoorayaccount.account.mapper;

import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;

public interface AccountMapper {
    LoginReqAccountResp toLoginReqAccountResp(LoginAccountProjection projection);
    AccountResp toAccountResp(Account account);
}
