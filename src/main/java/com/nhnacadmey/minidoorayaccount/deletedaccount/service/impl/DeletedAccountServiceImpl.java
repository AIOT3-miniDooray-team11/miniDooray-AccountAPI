package com.nhnacadmey.minidoorayaccount.deletedaccount.service.impl;

import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.deletedaccount.entity.DeletedAccount;
import com.nhnacadmey.minidoorayaccount.deletedaccount.repository.DeletedAccountRepository;
import com.nhnacadmey.minidoorayaccount.deletedaccount.service.DeletedAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DeletedAccountServiceImpl implements DeletedAccountService {
    private final DeletedAccountRepository deletedAccountRepository;

    @Override
    @Transactional
    public void register(Account account) {
        DeletedAccount deletedAccount = DeletedAccount.create(account);
        deletedAccountRepository.save(deletedAccount);
    }
}
