package com.servihabitat.auth.model;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class BodyToSend {
    private String grant_type;
    private String username;
    private String password;
}

