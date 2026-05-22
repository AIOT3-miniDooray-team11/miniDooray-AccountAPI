package com.nhnacadmey.minidoorayaccount.integration;

import com.nhnacadmey.minidoorayaccount.account.dto.request.AccountListReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import com.nhnacadmey.minidoorayaccount.account.repository.AccountRepository;
import com.nhnacadmey.minidoorayaccount.deletedaccount.repository.DeletedAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AccountIntegrationTest {

    private static final String BASE_URL = "/account-api/v1/accounts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DeletedAccountRepository deletedAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        deletedAccountRepository.deleteAll();
        accountRepository.deleteAll();
    }

    // ── 계정 등록 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("[통합] 계정 등록 → DB 저장, 비밀번호 인코딩 확인")
    void registerAccount_savesToDb_withEncodedPassword() throws Exception {
        CreateAccountReq req = new CreateAccountReq("user1", "rawPass", "user1@test.com", "홍길동");

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        Account saved = accountRepository.findAccountByUserId("user1");
        assertThat(saved).isNotNull();
        assertThat(passwordEncoder.matches("rawPass", saved.getUserPassword())).isTrue();
    }

    @Test
    @DisplayName("[통합] 동일 userId 중복 등록 → 400")
    void registerAccount_duplicate_returns400() throws Exception {
        CreateAccountReq req = new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동");

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── 계정 조회 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("[통합] 로그인용 계정 조회 - userId, status 확인")
    void getAccountByLoginId_returnsLoginInfo() throws Exception {
        registerUser("user1", "pass1", "user1@test.com", "홍길동");

        mockMvc.perform(get(BASE_URL + "/login").param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("[통합] 로그인용 계정 조회 - 없는 userId → 400")
    void getAccountByLoginId_notExist_returns400() throws Exception {
        mockMvc.perform(get(BASE_URL + "/login").param("userId", "ghost"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[통합] id로 계정 조회 - 필드 값 확인")
    void getAccountById_returnsAccount() throws Exception {
        registerUser("user1", "pass1", "user1@test.com", "홍길동");
        Account saved = accountRepository.findAccountByUserId("user1");

        mockMvc.perform(get(BASE_URL + "/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@test.com"));
    }

    @Test
    @DisplayName("[통합] id로 계정 조회 - 없는 id → 400")
    void getAccountById_notExist_returns400() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[통합] userId로 계정 조회")
    void getAccountByUserId_returnsAccount() throws Exception {
        registerUser("user1", "pass1", "user1@test.com", "홍길동");

        mockMvc.perform(get(BASE_URL).param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    @DisplayName("[통합] ID 목록으로 계정 리스트 조회")
    void getAccountList_returnsMultipleAccounts() throws Exception {
        registerUser("user1", "pass1", "user1@test.com", "홍길동");
        registerUser("user2", "pass2", "user2@test.com", "김철수");

        Account a1 = accountRepository.findAccountByUserId("user1");
        Account a2 = accountRepository.findAccountByUserId("user2");

        AccountListReq req = new AccountListReq(List.of(a1.getId(), a2.getId()));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountRespList.length()").value(2));
    }

    // ── 계정 수정 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("[통합] 계정 수정 → DB 반영 확인")
    void updateAccount_updatesDb() throws Exception {
        registerUser("user1", "pass1", "user1@test.com", "홍길동");
        Account saved = accountRepository.findAccountByUserId("user1");

        UpdateAccountReq req = new UpdateAccountReq("user1", "newPass", "new@test.com", "새이름");
        mockMvc.perform(put(BASE_URL + "/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        Account updated = accountRepository.findAccountById(saved.getId());
        assertThat(updated.getUserName()).isEqualTo("새이름");
        assertThat(updated.getUserEmail()).isEqualTo("new@test.com");
    }

    @Test
    @DisplayName("[통합] 계정 수정 - 없는 id → 400")
    void updateAccount_notExist_returns400() throws Exception {
        UpdateAccountReq req = new UpdateAccountReq("user1", "pass1", "user1@test.com", "홍길동");

        mockMvc.perform(put(BASE_URL + "/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── 계정 삭제 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("[통합] 계정 삭제 → DB에서 제거, deleted_accounts에 이력 저장")
    void deleteAccount_removesFromDbAndSavesHistory() throws Exception {
        registerUser("user1", "pass1", "user1@test.com", "홍길동");
        Account saved = accountRepository.findAccountByUserId("user1");

        mockMvc.perform(delete(BASE_URL + "/" + saved.getId()))
                .andExpect(status().isOk());

        assertThat(accountRepository.existsAccountByUserId("user1")).isFalse();
        assertThat(deletedAccountRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("[통합] 계정 삭제 - 없는 id → 400")
    void deleteAccount_notExist_returns400() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9999"))
                .andExpect(status().isBadRequest());
    }

    // ── helper ────────────────────────────────────────────────────────

    private void registerUser(String userId, String password, String email, String name)
            throws Exception {
        CreateAccountReq req = new CreateAccountReq(userId, password, email, name);
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}