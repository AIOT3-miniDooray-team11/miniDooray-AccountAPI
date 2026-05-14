package com.nhnacadmey.minidoorayaccount.account.dto.request;

public record UpdateAccountReq(
        String userId,
        String userPassword,
        String userEmail,
        String userName
) {
}
