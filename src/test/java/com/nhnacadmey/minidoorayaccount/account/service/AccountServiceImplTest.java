package com.nhnacadmey.minidoorayaccount.account.service;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountExistException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountInvalidInputException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountNotExistException;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import com.nhnacadmey.minidoorayaccount.account.repository.AccountRepository;
import com.nhnacadmey.minidoorayaccount.account.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    // ── isAccountByUserId ──────────────────────────────────────────────

    @Test
    @DisplayName("isAccountByUserId - 계정 존재 시 정상 통과")
    void isAccountByUserId_exists_noException() {
        when(accountRepository.existsAccountByUserId("user1")).thenReturn(true);
        assertThatCode(() -> accountService.isAccountByUserId("user1")).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("isAccountByUserId - 계정 없으면 AccountNotExistException")
    void isAccountByUserId_notExists_throws() {
        when(accountRepository.existsAccountByUserId("user1")).thenReturn(false);
        assertThatThrownBy(() -> accountService.isAccountByUserId("user1"))
                .isInstanceOf(AccountNotExistException.class);
    }

    @Test
    @DisplayName("isAccountByUserId - null 입력이면 AccountInvalidInputException")
    void isAccountByUserId_null_throws() {
        assertThatThrownBy(() -> accountService.isAccountByUserId(null))
                .isInstanceOf(AccountInvalidInputException.class);
        verifyNoInteractions(accountRepository);
    }

    @Test
    @DisplayName("isAccountByUserId - 공백 입력이면 AccountInvalidInputException")
    void isAccountByUserId_blank_throws() {
        assertThatThrownBy(() -> accountService.isAccountByUserId("  "))
                .isInstanceOf(AccountInvalidInputException.class);
        verifyNoInteractions(accountRepository);
    }

    // ── isNotAccountByUserId ───────────────────────────────────────────

    @Test
    @DisplayName("isNotAccountByUserId - 계정 없으면 정상 통과")
    void isNotAccountByUserId_notExists_noException() {
        when(accountRepository.existsAccountByUserId("user1")).thenReturn(false);
        assertThatCode(() -> accountService.isNotAccountByUserId("user1")).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("isNotAccountByUserId - 계정 존재하면 AccountExistException")
    void isNotAccountByUserId_exists_throws() {
        when(accountRepository.existsAccountByUserId("user1")).thenReturn(true);
        assertThatThrownBy(() -> accountService.isNotAccountByUserId("user1"))
                .isInstanceOf(AccountExistException.class);
    }

    // ── isAccountById ──────────────────────────────────────────────────

    @Test
    @DisplayName("isAccountById - 계정 존재 시 정상 통과")
    void isAccountById_exists_noException() {
        when(accountRepository.existsAccountById(1L)).thenReturn(true);
        assertThatCode(() -> accountService.isAccountById(1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("isAccountById - 계정 없으면 AccountNotExistException")
    void isAccountById_notExists_throws() {
        when(accountRepository.existsAccountById(1L)).thenReturn(false);
        assertThatThrownBy(() -> accountService.isAccountById(1L))
                .isInstanceOf(AccountNotExistException.class);
    }

    // ── getAccountProjectionByUserId ───────────────────────────────────

    @Test
    @DisplayName("getAccountProjectionByUserId - projection 반환")
    void getAccountProjectionByUserId_returnsProjection() {
        LoginAccountProjection projection = mock(LoginAccountProjection.class);
        when(accountRepository.findLoginAccountByUserId("user1")).thenReturn(projection);

        LoginAccountProjection result = accountService.getAccountProjectionByUserId("user1");

        assertThat(result).isSameAs(projection);
    }

    @Test
    @DisplayName("getAccountProjectionByUserId - null 입력이면 AccountInvalidInputException")
    void getAccountProjectionByUserId_null_throws() {
        assertThatThrownBy(() -> accountService.getAccountProjectionByUserId(null))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    // ── getAccountById ─────────────────────────────────────────────────

    @Test
    @DisplayName("getAccountById - account 반환")
    void getAccountById_returnsAccount() {
        Account account = Account.created(new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));
        when(accountRepository.findAccountById(1L)).thenReturn(account);

        Account result = accountService.getAccountById(1L);

        assertThat(result).isSameAs(account);
    }

    // ── getAccountsByIds ───────────────────────────────────────────────

    @Test
    @DisplayName("getAccountsByIds - 리스트 반환")
    void getAccountsByIds_returnsList() {
        Account account = Account.created(new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));
        when(accountRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(account));

        List<Account> result = accountService.getAccountsByIds(List.of(1L, 2L));

        assertThat(result).hasSize(1);
    }

    // ── registerAccount ────────────────────────────────────────────────

    @Test
    @DisplayName("registerAccount - 비밀번호 인코딩 후 저장")
    void registerAccount_encodesPasswordAndSaves() {
        CreateAccountReq req = new CreateAccountReq("user1", "rawPass", "user1@test.com", "홍길동");
        when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");

        accountService.registerAccount(req);

        verify(passwordEncoder).encode("rawPass");
        verify(accountRepository).save(argThat(a -> a.getUserPassword().equals("encodedPass")));
    }

    // ── updateAccount ──────────────────────────────────────────────────

    @Test
    @DisplayName("updateAccount - 변경된 필드 반영 후 저장")
    void updateAccount_updatesFieldsAndSaves() {
        Account account = Account.created(
                new CreateAccountReq("oldUser", "oldPass", "old@test.com", "구이름"));
        when(accountRepository.findAccountById(1L)).thenReturn(account);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        accountService.updateAccount(1L, new UpdateAccountReq("newUser", "newPass", "new@test.com", "새이름"));

        assertThat(account.getUserId()).isEqualTo("newUser");
        assertThat(account.getUserEmail()).isEqualTo("new@test.com");
        assertThat(account.getUserName()).isEqualTo("새이름");
        verify(accountRepository).save(account);
    }

    // ── deleteAccount ──────────────────────────────────────────────────

    @Test
    @DisplayName("deleteAccount - repository.delete 호출")
    void deleteAccount_callsRepositoryDelete() {
        Account account = Account.created(
                new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));

        accountService.deleteAccount(account);

        verify(accountRepository).delete(account);
    }
}