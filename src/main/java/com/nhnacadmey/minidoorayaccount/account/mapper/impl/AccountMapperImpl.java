package com.nhnacadmey.minidoorayaccount.account.mapper.impl;

import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.mapper.AccountMapper;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements AccountMapper {
    @Override
    public LoginReqAccountResp toLoginReqAccountResp(LoginAccountProjection projection) {
        return new LoginReqAccountResp(projection.id(), projection.userId(), projection.userPassword(), projection.status());
    }

    @Override
    public AccountResp toAccountResp(Account account) {
        return new AccountResp(account.getId(), account.getUserId(), account.getUserEmail(),
                account.getUserName(), account.getStatus(), account.getCreatedAT());
    }
}
