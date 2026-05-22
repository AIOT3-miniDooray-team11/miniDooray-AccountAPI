package com.nhnacadmey.minidoorayaccount.account.dto.response;

import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;

import java.time.LocalDateTime;

public record AccountResp(
        long id,
        String userId,
        String email,
        String Name,
        UserStatus status,
        LocalDateTime createAt
) {
}
