package com.nhnacadmey.minidoorayaccount.account.entity;

import com.nhnacadmey.minidoorayaccount.account.dto.request.CreateAccountReq;
import com.nhnacadmey.minidoorayaccount.account.execption.AccountInvalidInputException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(max = 30)
    @Column(name = "user_id", unique = true)
    private String userId;

    @Length(max = 100)
    @Column(name = "user_password")
    private String userPassword;

    @Pattern(
            regexp = "^[a-zA-Z0-9_+&*\\-]+(?:\\.[a-zA-Z0-9_+&*\\-]+)*@(?:[a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,7}$",
            message = "올바른 이메일 형식이 아닙니다."
    )
    @Length(max = 50)
    @Column(name = "user_email")
    private String userEmail;

    @Length(max = 50)
    @Column(name = "user_name")
    private String userName;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAT;

    private Account(String userId, String userPassword, String userEmail, String userName) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userName = userName;
        this.status = UserStatus.ACTIVE;
        this.createdAT = LocalDateTime.now();
    }

    public static Account created(CreateAccountReq dto){
        checkInputDto(dto);
        return new Account(dto.userId(), dto.userPassword(), dto.userEmail(), dto.userName());
    }

    private static void checkInputDto(Object o){
        String userId = null;
        String userPassword = null;
        String userEmail = null;
        String userName = null;
        if(o instanceof CreateAccountReq(String userId1, String password, String email, String name)){
            userId = userId1;
            userPassword = password;
            userEmail = email;
            userName = name;
        }

        if(Objects.isNull(userId) || userId.isBlank()) {
            throw new AccountInvalidInputException("user-id : 잘못된 입력입니다");
        }
        if(Objects.isNull(userPassword) || userPassword.isBlank()) {
            throw new AccountInvalidInputException("user-password : 잘못된 입력입니다");
        }
        if(Objects.isNull(userEmail) || userEmail.isBlank()) {
            throw new AccountInvalidInputException("user-email : 잘못된 입력입니다");
        }
        if(Objects.isNull(userName) || userName.isBlank()) {
            throw new AccountInvalidInputException("user-name : 잘못된 입력입니다");
        }
    }

    public void setUserId(String userId) {
        if(Objects.isNull(userId) || userId.isBlank()){
            throw new AccountInvalidInputException("user-id : 잘못된 입력입니다");
        }
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        if(Objects.isNull(userPassword) || userPassword.isBlank()) {
            throw new AccountInvalidInputException("user-password : 잘못된 입력입니다");
        }
        this.userPassword = userPassword;
    }

    public void setUserEmail(String userEmail) {
        if(Objects.isNull(userEmail) || userEmail.isBlank()) {
            throw new AccountInvalidInputException("user-email : 잘못된 입력입니다");
        }
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        if(Objects.isNull(userName) || userName.isBlank()) {
            throw new AccountInvalidInputException("user-name : 잘못된 입력입니다");
        }
        this.userName = userName;
    }

    public void setStatus(UserStatus status) {
        if(Objects.isNull(status)) {
            throw new AccountInvalidInputException("status : 잘못된 값 주입");
        }
        this.status = status;
    }
}
