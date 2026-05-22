package com.nhnacadmey.minidoorayaccount.account.dto.response;

import java.util.List;

public record AccountListResp(
        List<AccountResp> accountRespList
) {
}
