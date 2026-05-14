package com.nhnacadmey.minidoorayaccount.account.projection;

import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;

public record LoginAccountProjection(
        long id,
        String userId,
        String userPassword,
        UserStatus status
) {
}
