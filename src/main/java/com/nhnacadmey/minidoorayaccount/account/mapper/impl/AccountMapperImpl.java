package com.nhnacadmey.minidoorayaccount.account.mapper.impl;

import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.mapper.AccountMapper;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMapperImpl implements AccountMapper {
    @Override
    public LoginReqAccountResp toLoginReqAccountResp(LoginAccountProjection projection) {
        return new LoginReqAccountResp(projection.getId(), projection.getUserId(),
                projection.getUserPassword(), projection.getStatus());
    }

    @Override
    public AccountResp toAccountResp(Account account) {
        return new AccountResp(account.getId(), account.getUserId(), account.getUserEmail(),
                account.getUserName(), account.getStatus(), account.getCreatedAT());
    }

    @Override
    public AccountListResp toAccountListResp(List<Account> accounts) {
        return new AccountListResp(accounts.stream()
                .map(a ->
                        new AccountResp(a.getId(), a.getUserId(), a.getUserEmail(), a.getUserName(),
                                a.getStatus(), a.getCreatedAT()))
                .toList()
        );
    }
}
