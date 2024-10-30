package com.example.backoffice.domain.openBank.token;

import lombok.Getter;

@Getter
public class OpenBankResponseToken {
    private String rsp_code;
    private String rsp_message;
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String scope;
    private String user_seq_no;
}