package com.servihabitat.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class IbmTokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
    private String scope;
}
