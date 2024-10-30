package com.example.backoffice.domain.openBank.service;

import com.example.backoffice.domain.openBank.dto.TokenRequestDto;
import com.example.backoffice.domain.openBank.token.OpenBankRequestToken;
import com.example.backoffice.domain.openBank.token.OpenBankResponseToken;

public interface OpenBankApiServiceV1 {

    OpenBankResponseToken requestToken(OpenBankRequestToken requestToken);
}
