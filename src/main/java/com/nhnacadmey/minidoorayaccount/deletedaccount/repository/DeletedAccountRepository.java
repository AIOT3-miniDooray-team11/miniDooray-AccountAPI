package com.nhnacadmey.minidoorayaccount.deletedaccount.repository;

import com.nhnacadmey.minidoorayaccount.deletedaccount.entity.DeletedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedAccountRepository extends JpaRepository<DeletedAccount, Long> {
}
