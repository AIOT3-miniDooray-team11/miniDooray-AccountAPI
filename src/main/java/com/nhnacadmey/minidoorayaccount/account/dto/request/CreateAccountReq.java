package com.nhnacadmey.minidoorayaccount.account.dto.request;

public record CreateAccountReq(
        String userId,
        String userPassword,
        String userEmail,
        String userName
) {
}
