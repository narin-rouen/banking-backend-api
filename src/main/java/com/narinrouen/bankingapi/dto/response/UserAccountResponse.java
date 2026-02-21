package com.narinrouen.bankingapi.dto.response;

import java.util.List;

public record UserAccountResponse(UserSummaryResponse user, List<AccountDto> account) {

}
