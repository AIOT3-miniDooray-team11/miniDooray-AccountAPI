package com.nhnacadmey.minidoorayaccount.account.projection;

import com.nhnacadmey.minidoorayaccount.account.entity.UserStatus;

public interface LoginAccountProjection{
        long getId();
        String getUserId();
        String getUserPassword();
        UserStatus getStatus();
}
