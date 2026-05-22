package com.nhnacadmey.minidoorayaccount.account.mapper;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;
import com.nhnacadmey.minidoorayaccount.account.mapper.impl.AccountMapperImpl;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountMapperImplTest {

    private AccountMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new AccountMapperImpl();
    }

    @Test
    @DisplayName("LoginAccountProjection -> LoginReqAccountResp 변환")
    void toLoginReqAccountResp_mapsAllFields() {
        LoginAccountProjection projection = mock(LoginAccountProjection.class);
        when(projection.getId()).thenReturn(1L);
        when(projection.getUserId()).thenReturn("user1");
        when(projection.getUserPassword()).thenReturn("encodedPw");
        when(projection.getStatus()).thenReturn(UserStatus.ACTIVE);

        LoginReqAccountResp result = mapper.toLoginReqAccountResp(projection);

        assertThat(result.accountId()).isEqualTo(1L);
        assertThat(result.userId()).isEqualTo("user1");
        assertThat(result.userPassword()).isEqualTo("encodedPw");
        assertThat(result.status()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Account -> AccountResp 변환 - 모든 필드 매핑")
    void toAccountResp_mapsAllFields() {
        Account account = Account.created(
                new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"));

        AccountResp result = mapper.toAccountResp(account);

        assertThat(result.userId()).isEqualTo("user1");
        assertThat(result.email()).isEqualTo("user1@test.com");
        assertThat(result.Name()).isEqualTo("홍길동");
        assertThat(result.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.createAt()).isNotNull();
    }

    @Test
    @DisplayName("List<Account> -> AccountListResp 변환 - 사이즈와 순서 유지")
    void toAccountListResp_mapsList() {
        List<Account> accounts = List.of(
                Account.created(new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동")),
                Account.created(new CreateAccountReq("user2", "pass2", "user2@test.com", "김철수"))
        );

        AccountListResp result = mapper.toAccountListResp(accounts);

        assertThat(result.accountRespList()).hasSize(2);
        assertThat(result.accountRespList().get(0).userId()).isEqualTo("user1");
        assertThat(result.accountRespList().get(1).userId()).isEqualTo("user2");
    }

    @Test
    @DisplayName("빈 List -> AccountListResp 변환 - 빈 리스트")
    void toAccountListResp_emptyList() {
        AccountListResp result = mapper.toAccountListResp(List.of());
        assertThat(result.accountRespList()).isEmpty();
    }
}