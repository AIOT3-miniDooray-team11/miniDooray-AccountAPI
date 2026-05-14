package com.nhnacadmey.minidoorayaccount.account.dto;

public record CreateAccountDto(
        String userId,
        String userPassword,
        String userEmail,
        String userName
) {
}
