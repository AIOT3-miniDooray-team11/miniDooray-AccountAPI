package com.nhnacadmey.minidoorayaccount.account.service.impl;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountExistException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountInvalidInputException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountNotExistException;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import com.nhnacadmey.minidoorayaccount.account.repository.AccountRepository;
import com.nhnacadmey.minidoorayaccount.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public void isAccountByUserId(String userId) {
        isNotNullCheck(userId);

        if(!accountRepository.existsAccountByUserId(userId)){
            log.debug("[account service] 존재하지 않는 계정입니다 - userId:{}", userId);
            throw new AccountNotExistException("[account service] 존재하지 않는 계정입니다");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void isNotAccountByUserId(String userId) {
        isNotNullCheck(userId);

        if(accountRepository.existsAccountByUserId(userId)){
            log.debug("[account service] 존재하는 계정입니다 - userId:{}", userId);
            throw new AccountExistException("[account service] 존재하는 계정입니다");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void isAccountById(long accountId) {
        if(!accountRepository.existsAccountById(accountId)){
            log.debug("[account service] 존재하지 않는 계정입니다 - id:{}", accountId);
            throw new AccountNotExistException("[account service] 존재하지 않는 계정입니다");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LoginAccountProjection getAccountProjectionByUserId(String userId) {
        isNotNullCheck(userId);
        return accountRepository.findLoginAccountByUserId(userId);
    }

    private void isNotNullCheck(String str) {
        if(Objects.isNull(str) || str.isBlank()) {
            log.debug("[account service] 문자열이 비어있습니다.");
            throw new AccountInvalidInputException("[account service] 문자열이 비어있습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(long accountId) {
        return accountRepository.findAccountById(accountId);
    }

    @Override
    public List<Account> getAccountsByIds(List<Long> accountIds) {
        return accountRepository.findAllByIdIn(accountIds);
    }

    @Override
    public Account getAccountByUserId(String userId) {
        return accountRepository.findAccountByUserId(userId);
    }

    @Override
    @Transactional
    public void registerAccount(CreateAccountReq req) {
        Account account = Account.created(req);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void updateAccount(long accountId, UpdateAccountReq req) {
        Account account = accountRepository.findAccountById(accountId);

        if(!account.getUserId().equals(req.userId())){
            account.setUserId(req.userId());
        }
        if(!account.getUserPassword().equals(req.userPassword())){
            account.setUserPassword(req.userPassword());
        }
        if(!account.getUserEmail().equals(req.userEmail())){
            account.setUserEmail(req.userEmail());
        }
        if(!account.getUserName().equals(req.userName())){
            account.setUserName(req.userName());
        }

        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }
}
