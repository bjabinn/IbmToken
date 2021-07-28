package com.servihabitat.auth.model;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class EntityIncoming {
    private String clientId;
    private String secret;
    private String serviceAccountName;
    private String serviceAccountPass;
}


