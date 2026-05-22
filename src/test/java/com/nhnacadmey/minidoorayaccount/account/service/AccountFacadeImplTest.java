package com.nhnacadmey.minidoorayaccount.account.service;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;
import com.nhnacadmey.minidoorayaccount.account.mapper.AccountMapper;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import com.nhnacadmey.minidoorayaccount.account.service.impl.AccountFacadeImpl;
import com.nhnacadmey.minidoorayaccount.deletedaccount.service.DeletedAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.InOrder;

@ExtendWith(MockitoExtension.class)
class AccountFacadeImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private DeletedAccountService deletedAccountService;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountFacadeImpl accountFacade;

    // ── getAccountByLoginId ────────────────────────────────────────────

    @Test
    @DisplayName("getAccountByLoginId - isAccountByUserId 검증 후 Projection 매핑 반환")
    void getAccountByLoginId_success() {
        LoginAccountProjection projection = mock(LoginAccountProjection.class);
        LoginReqAccountResp expected = new LoginReqAccountResp(1L, "user1", "pw", UserStatus.ACTIVE);

        doNothing().when(accountService).isAccountByUserId("user1");
        when(accountService.getAccountProjectionByUserId("user1")).thenReturn(projection);
        when(accountMapper.toLoginReqAccountResp(projection)).thenReturn(expected);

        LoginReqAccountResp result = accountFacade.getAccountByLoginId("user1");

        assertThat(result).isEqualTo(expected);
        verify(accountService).isAccountByUserId("user1");
        verify(accountMapper).toLoginReqAccountResp(projection);
    }

    // ── getAccountById ─────────────────────────────────────────────────

    @Test
    @DisplayName("getAccountById - isAccountById 검증 후 Account 매핑 반환")
    void getAccountById_success() {
        Account account = Account.created(
                new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));
        AccountResp expected = new AccountResp(1L, "user1", "user1@test.com", "홍길동",
                UserStatus.ACTIVE, LocalDateTime.now());

        doNothing().when(accountService).isAccountById(1L);
        when(accountService.getAccountById(1L)).thenReturn(account);
        when(accountMapper.toAccountResp(account)).thenReturn(expected);

        AccountResp result = accountFacade.getAccountById(1L);

        assertThat(result).isEqualTo(expected);
        verify(accountService).isAccountById(1L);
    }

    // ── getAccountByIds ────────────────────────────────────────────────

    @Test
    @DisplayName("getAccountByIds - 다수 ID로 리스트 반환")
    void getAccountByIds_returnsList() {
        List<Account> accounts = List.of(
                Account.created(new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"))
        );
        AccountListResp expected = new AccountListResp(List.of());

        when(accountService.getAccountsByIds(List.of(1L))).thenReturn(accounts);
        when(accountMapper.toAccountListResp(accounts)).thenReturn(expected);

        AccountListResp result = accountFacade.getAccountByIds(List.of(1L));

        assertThat(result).isEqualTo(expected);
    }

    // ── getAccountByUserId ─────────────────────────────────────────────

    @Test
    @DisplayName("getAccountByUserId - isAccountByUserId 검증 후 Account 매핑 반환")
    void getAccountByUserId_success() {
        Account account = Account.created(
                new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));
        AccountResp expected = new AccountResp(1L, "user1", "user1@test.com", "홍길동",
                UserStatus.ACTIVE, LocalDateTime.now());

        doNothing().when(accountService).isAccountByUserId("user1");
        when(accountService.getAccountByUserId("user1")).thenReturn(account);
        when(accountMapper.toAccountResp(account)).thenReturn(expected);

        AccountResp result = accountFacade.getAccountByUserId("user1");

        assertThat(result).isEqualTo(expected);
        verify(accountService).isAccountByUserId("user1");
    }

    // ── registerAccount ────────────────────────────────────────────────

    @Test
    @DisplayName("registerAccount - 중복 체크 후 등록 호출")
    void registerAccount_checksNotExistThenRegisters() {
        CreateAccountReq req = new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동");
        doNothing().when(accountService).isNotAccountByUserId("user1");
        doNothing().when(accountService).registerAccount(req);

        assertThatCode(() -> accountFacade.registerAccount(req)).doesNotThrowAnyException();

        verify(accountService).isNotAccountByUserId("user1");
        verify(accountService).registerAccount(req);
    }

    // ── updateAccount ──────────────────────────────────────────────────

    @Test
    @DisplayName("updateAccount - 존재 검증 후 수정 호출")
    void updateAccount_checksExistsThenUpdates() {
        UpdateAccountReq req = new UpdateAccountReq("newUser", "newPass", "new@test.com", "새이름");
        doNothing().when(accountService).isAccountById(1L);
        doNothing().when(accountService).updateAccount(1L, req);

        assertThatCode(() -> accountFacade.updateAccount(1L, req)).doesNotThrowAnyException();

        verify(accountService).isAccountById(1L);
        verify(accountService).updateAccount(1L, req);
    }

    // ── deleteAccount ──────────────────────────────────────────────────

    @Test
    @DisplayName("deleteAccount - 삭제 계정 이력 저장 후 삭제")
    void deleteAccount_registersDeletedThenDeletes() {
        Account account = Account.created(
                new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));
        doNothing().when(accountService).isAccountById(1L);
        when(accountService.getAccountById(1L)).thenReturn(account);
        doNothing().when(deletedAccountService).register(account);
        doNothing().when(accountService).deleteAccount(account);

        assertThatCode(() -> accountFacade.deleteAccount(1L)).doesNotThrowAnyException();

        InOrder inOrder = inOrder(deletedAccountService, accountService);
        inOrder.verify(deletedAccountService).register(account);
        inOrder.verify(accountService).deleteAccount(account);
    }
}