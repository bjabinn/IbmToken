package com.servihabitat.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class EntityIncoming {
    public String clientId;
    public String secret;
    public String serviceAccountName;
    public String serviceAccountPass;

    public EntityIncoming(String cliente, String secr, String serviceName, String servicePass) {
        clientId = cliente;
        secret = secr;
        serviceAccountName = serviceName;
        serviceAccountPass = servicePass;
    }
}


