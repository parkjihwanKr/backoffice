package com.example.backoffice.domain.openBank.service;

import com.example.backoffice.domain.openBank.token.OpenBankRequestToken;
import com.example.backoffice.domain.openBank.token.OpenBankResponseToken;
import com.example.backoffice.domain.openBank.utils.OpenBankUtil;
import com.example.backoffice.global.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OpenBankApiServiceImplV1 implements OpenBankApiServiceV1 {

    private final RestTemplate restTemplate;

    public OpenBankApiServiceImplV1(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public OpenBankResponseToken requestToken(OpenBankRequestToken openBankRequestToken){
        HttpHeaders httpHeaders
                = generateHeader(OpenBankUtil.CONTENT_TYPE, OpenBankUtil.FORM_URLENCODED);

        HttpEntity httpEntity
                = generateHttpEntityWithBody(
                        httpHeaders, openBankRequestToken.toMultiValueMap());

        OpenBankResponseToken openBankResponseToken
                = restTemplate.exchange(
                        OpenBankUtil.BASE_URL + OpenBankUtil.OAUTH2_TOKEN,
                HttpMethod.POST, httpEntity, OpenBankResponseToken.class).getBody();

        if(isErrorCode(openBankResponseToken.getRsp_code())){
            log.error("error code : {}, error msg : {}", openBankResponseToken.getRsp_code(), openBankResponseToken.getRsp_message());
            throw new RuntimeException(openBankResponseToken.getRsp_message());
        }
        return openBankResponseToken;

    }
    private HttpEntity generateHttpEntityWithBody(HttpHeaders httpHeaders, MultiValueMap body) {
        return new HttpEntity<>(body, httpHeaders);
    }

    private HttpHeaders generateHeader(String name ,String val){
        HttpHeaders httpHeaders = new HttpHeaders();
        String bearerToken = JwtProvider.BEARER_PREFIX+val;
        if (name.equals("Authorization")) {
            httpHeaders.add(name, bearerToken);
            return httpHeaders;
        }
        // name, tokenValue
        httpHeaders.add(name, val);
        return httpHeaders;
    }

    private boolean isErrorCode(String code){
        if (code==null) return false;
        if (code.equals(OpenBankUtil.SUCCESS_CODE)){
            return false;
        }
        return true;
    }
}
