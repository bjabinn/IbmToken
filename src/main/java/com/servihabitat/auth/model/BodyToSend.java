package com.servihabitat.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class BodyToSend {
    private String grant_type;
    private String username;
    private String password;

    public BodyToSend(String grantType, String user, String pass) {
        grant_type = grantType;
        username = user;
        password = pass;
    }
}

