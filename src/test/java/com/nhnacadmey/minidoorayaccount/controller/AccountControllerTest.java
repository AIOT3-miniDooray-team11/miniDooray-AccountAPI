package com.nhnacadmey.minidoorayaccount.controller;

import com.nhnacadmey.minidoorayaccount.account.dto.request.AccountListReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.request.UpdateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountListResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.AccountResp;
import com.nhnacadmey.minidoorayaccount.account.dto.response.LoginReqAccountResp;
import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountExistException;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountNotExistException;
import com.nhnacadmey.minidoorayaccount.account.service.AccountFacade;
import com.nhnacadmey.minidoorayaccount.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@Import(SecurityConfig.class)
class AccountControllerTest {

    private static final String BASE_URL = "/account-api/v1/accounts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountFacade accountFacade;

    // ── GET /login ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /login - 정상 조회 200")
    void getAccountByLoginId_returns200() throws Exception {
        LoginReqAccountResp resp = new LoginReqAccountResp(1L, "user1", "encodedPw", UserStatus.ACTIVE);
        when(accountFacade.getAccountByLoginId("user1")).thenReturn(resp);

        mockMvc.perform(get(BASE_URL + "/login").param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("GET /login - 존재하지 않는 계정 → 404")
    void getAccountByLoginId_notExist_returns404() throws Exception {
        when(accountFacade.getAccountByLoginId("ghost"))
                .thenThrow(new AccountNotExistException("존재하지 않는 계정"));

        mockMvc.perform(get(BASE_URL + "/login").param("userId", "ghost"))
                .andExpect(status().isNotFound());
    }

    // ── GET /{id} ──────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /{id} - 정상 조회 200")
    void getAccountById_returns200() throws Exception {
        AccountResp resp = new AccountResp(1L, "user1", "user1@test.com", "홍길동",
                UserStatus.ACTIVE, LocalDateTime.now());
        when(accountFacade.getAccountById(1L)).thenReturn(resp);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    @DisplayName("GET /{id} - 존재하지 않는 id → 404")
    void getAccountById_notExist_returns404() throws Exception {
        when(accountFacade.getAccountById(999L))
                .thenThrow(new AccountNotExistException("존재하지 않는 계정"));

        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    // ── GET /?userId= ──────────────────────────────────────────────────

    @Test
    @DisplayName("GET /?userId= - 정상 조회 200")
    void getAccountByUserId_returns200() throws Exception {
        AccountResp resp = new AccountResp(1L, "user1", "user1@test.com", "홍길동",
                UserStatus.ACTIVE, LocalDateTime.now());
        when(accountFacade.getAccountByUserId("user1")).thenReturn(resp);

        mockMvc.perform(get(BASE_URL).param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    // ── POST / (계정 목록 조회) ────────────────────────────────────────

    @Test
    @DisplayName("POST / - 계정 목록 조회 200")
    void getAccountList_returns200() throws Exception {
        AccountListResp resp = new AccountListResp(List.of(
                new AccountResp(1L, "user1", "user1@test.com", "홍길동", UserStatus.ACTIVE, LocalDateTime.now())
        ));
        when(accountFacade.getAccountByIds(List.of(1L))).thenReturn(resp);

        AccountListReq req = new AccountListReq(List.of(1L));
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountRespList[0].userId").value("user1"));
    }

    // ── POST /register ─────────────────────────────────────────────────

    @Test
    @DisplayName("POST /register - 계정 등록 성공 201")
    void registerAccount_returns201() throws Exception {
        CreateAccountReq req = new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동");
        doNothing().when(accountFacade).registerAccount(req);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(accountFacade).registerAccount(req);
    }

    @Test
    @DisplayName("POST /register - 중복 계정 → 409")
    void registerAccount_duplicate_returns409() throws Exception {
        CreateAccountReq req = new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동");
        doThrow(new AccountExistException("존재하는 계정")).when(accountFacade).registerAccount(req);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    // ── PUT /{id} ──────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /{id} - 계정 수정 성공 204")
    void updateAccount_returns204() throws Exception {
        UpdateAccountReq req = new UpdateAccountReq("newUser", "newPass", "new@test.com", "새이름");
        doNothing().when(accountFacade).updateAccount(1L, req);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        verify(accountFacade).updateAccount(1L, req);
    }

    @Test
    @DisplayName("PUT /{id} - 존재하지 않는 계정 수정 → 404")
    void updateAccount_notExist_returns404() throws Exception {
        UpdateAccountReq req = new UpdateAccountReq("newUser", "newPass", "new@test.com", "새이름");
        doThrow(new AccountNotExistException("존재하지 않는 계정"))
                .when(accountFacade).updateAccount(999L, req);

        mockMvc.perform(put(BASE_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /{id} ───────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /{id} - 계정 삭제 성공 204")
    void deleteAccount_returns204() throws Exception {
        doNothing().when(accountFacade).deleteAccount(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(accountFacade).deleteAccount(1L);
    }

    @Test
    @DisplayName("DELETE /{id} - 존재하지 않는 계정 삭제 → 404")
    void deleteAccount_notExist_returns404() throws Exception {
        doThrow(new AccountNotExistException("존재하지 않는 계정"))
                .when(accountFacade).deleteAccount(999L);

        mockMvc.perform(delete(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }
}