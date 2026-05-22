package com.nhnacadmey.minidoorayaccount.deletedaccount.service;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.deletedaccount.entity.DeletedAccount;
import com.nhnacadmey.minidoorayaccount.deletedaccount.repository.DeletedAccountRepository;
import com.nhnacadmey.minidoorayaccount.deletedaccount.service.impl.DeletedAccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletedAccountServiceImplTest {

    @Mock
    private DeletedAccountRepository deletedAccountRepository;

    @InjectMocks
    private DeletedAccountServiceImpl deletedAccountService;

    @Test
    @DisplayName("register - Account로부터 DeletedAccount 생성 후 저장")
    void register_savesDeletedAccount() {
        Account account = Account.created(
                new CreateAccountReq("user1", "encodedPw", "user1@test.com", "홍길동"));

        deletedAccountService.register(account);

        ArgumentCaptor<DeletedAccount> captor = ArgumentCaptor.forClass(DeletedAccount.class);
        verify(deletedAccountRepository).save(captor.capture());

        DeletedAccount saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo("user1");
        assertThat(saved.getUserPassword()).isEqualTo("encodedPw");
        assertThat(saved.getUserEmail()).isEqualTo("user1@test.com");
        assertThat(saved.getUserName()).isEqualTo("홍길동");
        assertThat(saved.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("register - repository.save 정확히 1번 호출")
    void register_callsSaveOnce() {
        Account account = Account.created(
                new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));

        deletedAccountService.register(account);

        verify(deletedAccountRepository, times(1)).save(any(DeletedAccount.class));
    }
}