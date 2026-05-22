package com.nhnacadmey.minidoorayaccount.account.repository;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;
import com.nhnacadmey.minidoorayaccount.account.projection.LoginAccountProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account savedAccount;

    @BeforeEach
    void setUp() {
        savedAccount = accountRepository.save(
                Account.created(new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동"))
        );
    }

    // ── existsAccountByUserId ──────────────────────────────────────────

    @Test
    @DisplayName("existsAccountByUserId - 존재하는 userId → true")
    void existsAccountByUserId_exists_returnsTrue() {
        assertThat(accountRepository.existsAccountByUserId("user1")).isTrue();
    }

    @Test
    @DisplayName("existsAccountByUserId - 존재하지 않는 userId → false")
    void existsAccountByUserId_notExists_returnsFalse() {
        assertThat(accountRepository.existsAccountByUserId("unknown")).isFalse();
    }

    // ── existsAccountById ──────────────────────────────────────────────

    @Test
    @DisplayName("existsAccountById - 존재하는 id → true")
    void existsAccountById_exists_returnsTrue() {
        assertThat(accountRepository.existsAccountById(savedAccount.getId())).isTrue();
    }

    @Test
    @DisplayName("existsAccountById - 존재하지 않는 id → false")
    void existsAccountById_notExists_returnsFalse() {
        assertThat(accountRepository.existsAccountById(9999L)).isFalse();
    }

    // ── findAccountById ────────────────────────────────────────────────

    @Test
    @DisplayName("findAccountById - 정상 조회")
    void findAccountById_returnsAccount() {
        Account found = accountRepository.findAccountById(savedAccount.getId());
        assertThat(found).isNotNull();
        assertThat(found.getUserId()).isEqualTo("user1");
        assertThat(found.getUserEmail()).isEqualTo("user1@test.com");
    }

    // ── findAccountByUserId ────────────────────────────────────────────

    @Test
    @DisplayName("findAccountByUserId - 정상 조회")
    void findAccountByUserId_returnsAccount() {
        Account found = accountRepository.findAccountByUserId("user1");
        assertThat(found).isNotNull();
        assertThat(found.getUserEmail()).isEqualTo("user1@test.com");
        assertThat(found.getUserName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("findAccountByUserId - 존재하지 않으면 null")
    void findAccountByUserId_notExists_returnsNull() {
        Account found = accountRepository.findAccountByUserId("ghost");
        assertThat(found).isNull();
    }

    // ── findAllByIdIn ──────────────────────────────────────────────────

    @Test
    @DisplayName("findAllByIdIn - 다수 ID 조회")
    void findAllByIdIn_returnsMatchingAccounts() {
        Account second = accountRepository.save(
                Account.created(new CreateAccountReq("user2", "pass2", "user2@test.com", "김철수"))
        );

        List<Account> found = accountRepository.findAllByIdIn(
                List.of(savedAccount.getId(), second.getId()));

        assertThat(found).hasSize(2)
                .extracting(Account::getUserId)
                .containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    @DisplayName("findAllByIdIn - 일치하지 않는 ID만 넘기면 빈 리스트")
    void findAllByIdIn_noMatch_returnsEmpty() {
        List<Account> found = accountRepository.findAllByIdIn(List.of(9999L));
        assertThat(found).isEmpty();
    }

    // ── findLoginAccountByUserId (JPQL Projection) ─────────────────────

    @Test
    @DisplayName("findLoginAccountByUserId - Projection 필드 정상 반환")
    void findLoginAccountByUserId_returnsProjection() {
        LoginAccountProjection projection = accountRepository.findLoginAccountByUserId("user1");

        assertThat(projection).isNotNull();
        assertThat(projection.getId()).isEqualTo(savedAccount.getId());
        assertThat(projection.getUserId()).isEqualTo("user1");
        assertThat(projection.getUserPassword()).isEqualTo("pass1");
        assertThat(projection.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("findLoginAccountByUserId - 존재하지 않으면 null")
    void findLoginAccountByUserId_notExists_returnsNull() {
        LoginAccountProjection projection = accountRepository.findLoginAccountByUserId("ghost");
        assertThat(projection).isNull();
    }

    // ── save / delete ──────────────────────────────────────────────────

    @Test
    @DisplayName("save - 저장 후 ID 부여됨")
    void save_assignsId() {
        Account account = accountRepository.save(
                Account.created(new CreateAccountReq("user3", "pass3", "user3@test.com", "이영희"))
        );
        assertThat(account.getId()).isPositive();
    }

    @Test
    @DisplayName("delete - 삭제 후 존재하지 않음")
    void delete_removesAccount() {
        accountRepository.delete(savedAccount);
        assertThat(accountRepository.existsAccountByUserId("user1")).isFalse();
    }
}