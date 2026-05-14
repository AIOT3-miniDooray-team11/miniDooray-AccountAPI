package com.nhnacadmey.minidoorayaccount.account.dto;

public record UpdateAccountDto(
        String userId,
        String userPassword,
        String userEmail,
        String userName
) {
}
