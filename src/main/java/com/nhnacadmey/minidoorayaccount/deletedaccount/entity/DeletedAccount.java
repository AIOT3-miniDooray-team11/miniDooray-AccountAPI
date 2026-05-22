package com.nhnacadmey.minidoorayaccount.deletedaccount.entity;

import com.nhnacadmey.minidoorayaccount.account.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "deleted_accounts")
public class DeletedAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(max = 30)
    @Column(name = "user_id", unique = true)
    private String userId;

    @Length(max = 100)
    @Column(name = "user_password")
    private String userPassword;

    @Length(max = 50)
    @Column(name = "user_email")
    private String userEmail;

    @Length(max = 50)
    @Column(name = "user_name")
    private String userName;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private DeletedAccount(String userId, String userPassword, String userEmail, String userName){
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userName = userName;
        this.deletedAt = LocalDateTime.now();
    }

    public static DeletedAccount create(Account account) {
        return new DeletedAccount(account.getUserId(), account.getUserPassword(), account.getUserEmail(), account.getUserName());
    }
}
