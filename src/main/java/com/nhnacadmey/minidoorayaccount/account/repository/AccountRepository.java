package com.nhnacadmey.minidoorayaccount.account.repository;

import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
