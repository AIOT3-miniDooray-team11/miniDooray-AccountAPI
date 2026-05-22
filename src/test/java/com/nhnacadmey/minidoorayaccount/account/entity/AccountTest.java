package com.nhnacadmey.minidoorayaccount.account.entity;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountInvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    private static final CreateAccountReq VALID_REQ =
            new CreateAccountReq("user1", "pass1", "user1@test.com", "홍길동");

    @Test
    @DisplayName("정상 입력으로 Account 생성 - ACTIVE 상태, createdAT 설정")
    void created_validInput_success() {
        Account account = Account.created(VALID_REQ);

        assertThat(account.getUserId()).isEqualTo("user1");
        assertThat(account.getUserPassword()).isEqualTo("pass1");
        assertThat(account.getUserEmail()).isEqualTo("user1@test.com");
        assertThat(account.getUserName()).isEqualTo("홍길동");
        assertThat(account.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(account.getCreatedAT()).isNotNull();
    }

    @Test
    @DisplayName("userId가 null이면 AccountInvalidInputException")
    void created_nullUserId_throws() {
        CreateAccountReq req = new CreateAccountReq(null, "pass1", "user1@test.com", "홍길동");
        assertThatThrownBy(() -> Account.created(req))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("userId가 공백이면 AccountInvalidInputException")
    void created_blankUserId_throws() {
        CreateAccountReq req = new CreateAccountReq("  ", "pass1", "user1@test.com", "홍길동");
        assertThatThrownBy(() -> Account.created(req))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("userPassword가 null이면 AccountInvalidInputException")
    void created_nullPassword_throws() {
        CreateAccountReq req = new CreateAccountReq("user1", null, "user1@test.com", "홍길동");
        assertThatThrownBy(() -> Account.created(req))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("userEmail이 null이면 AccountInvalidInputException")
    void created_nullEmail_throws() {
        CreateAccountReq req = new CreateAccountReq("user1", "pass1", null, "홍길동");
        assertThatThrownBy(() -> Account.created(req))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("userName이 null이면 AccountInvalidInputException")
    void created_nullUserName_throws() {
        CreateAccountReq req = new CreateAccountReq("user1", "pass1", "user1@test.com", null);
        assertThatThrownBy(() -> Account.created(req))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("setUserId - 정상 변경")
    void setUserId_success() {
        Account account = Account.created(VALID_REQ);
        account.setUserId("newUser");
        assertThat(account.getUserId()).isEqualTo("newUser");
    }

    @Test
    @DisplayName("setUserId - null이면 AccountInvalidInputException")
    void setUserId_null_throws() {
        Account account = Account.created(VALID_REQ);
        assertThatThrownBy(() -> account.setUserId(null))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("setUserId - 공백이면 AccountInvalidInputException")
    void setUserId_blank_throws() {
        Account account = Account.created(VALID_REQ);
        assertThatThrownBy(() -> account.setUserId(" "))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("setUserPassword - 정상 변경")
    void setUserPassword_success() {
        Account account = Account.created(VALID_REQ);
        account.setUserPassword("newPass");
        assertThat(account.getUserPassword()).isEqualTo("newPass");
    }

    @Test
    @DisplayName("setUserPassword - null이면 AccountInvalidInputException")
    void setUserPassword_null_throws() {
        Account account = Account.created(VALID_REQ);
        assertThatThrownBy(() -> account.setUserPassword(null))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("setUserEmail - 정상 변경")
    void setUserEmail_success() {
        Account account = Account.created(VALID_REQ);
        account.setUserEmail("new@test.com");
        assertThat(account.getUserEmail()).isEqualTo("new@test.com");
    }

    @Test
    @DisplayName("setUserEmail - null이면 AccountInvalidInputException")
    void setUserEmail_null_throws() {
        Account account = Account.created(VALID_REQ);
        assertThatThrownBy(() -> account.setUserEmail(null))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("setUserName - 정상 변경")
    void setUserName_success() {
        Account account = Account.created(VALID_REQ);
        account.setUserName("새이름");
        assertThat(account.getUserName()).isEqualTo("새이름");
    }

    @Test
    @DisplayName("setUserName - null이면 AccountInvalidInputException")
    void setUserName_null_throws() {
        Account account = Account.created(VALID_REQ);
        assertThatThrownBy(() -> account.setUserName(null))
                .isInstanceOf(AccountInvalidInputException.class);
    }

    @Test
    @DisplayName("setStatus - DORMANT으로 변경")
    void setStatus_toDormant() {
        Account account = Account.created(VALID_REQ);
        account.setStatus(UserStatus.DORMANT);
        assertThat(account.getStatus()).isEqualTo(UserStatus.DORMANT);
    }

    @Test
    @DisplayName("setStatus - null이면 AccountInvalidInputException")
    void setStatus_null_throws() {
        Account account = Account.created(VALID_REQ);
        assertThatThrownBy(() -> account.setStatus(null))
                .isInstanceOf(AccountInvalidInputException.class);
    }
}