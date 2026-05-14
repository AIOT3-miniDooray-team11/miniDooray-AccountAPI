package com.nhnacadmey.minidoorayaccount.account.dto.response;

import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;

public record LoginReqAccountResp(
        long accountId,
        String userId,
        String userPassword,
        UserStatus status
) {
}
