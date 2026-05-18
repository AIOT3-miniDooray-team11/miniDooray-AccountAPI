package com.nhnacadmey.minidoorayaccount.account.dto.request;

import java.util.List;

public record AccountListReq(
        List<Long> accountIdList
) {
}
