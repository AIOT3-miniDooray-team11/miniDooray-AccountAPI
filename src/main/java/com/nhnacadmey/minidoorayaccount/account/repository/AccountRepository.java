package com.nhnacadmey.minidoorayaccount.account.repository;

import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a.id id, a.userId userId, a.userPassword userPassword, a.status status FROM Account a WHERE a.userId = ?1")
    LoginAccountProjection findLoginAccountByUserId(String userId);

    boolean existsAccountByUserId(String userId);

    Account findAccountById(long id);

    boolean existsAccountById(long id);

    List<Account> findAllByIdIn(List<Long> ids);
}
