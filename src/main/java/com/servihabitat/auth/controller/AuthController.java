package com.servihabitat.auth.controller;

import com.servihabitat.auth.model.BodyToSend;
import com.servihabitat.auth.model.EntityIncoming;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Map;


@RestController
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${URL_CLOUD_IBM_TOKEN}")
    private String urlIbmCloudToken;

    @Value("${TENANT_ID}")
    private String tenantId;

    @RequestMapping("/token")
    public ResponseEntity gettingAccessToken(@RequestBody EntityIncoming entityIn, HttpServletResponse res) {

        log.info("@@@@@@@@@@@@@@@@@@@ Inside token method");

        if (entityIn.clientId == "" || entityIn.secret == null || entityIn.serviceAccountName == "" || entityIn.serviceAccountPass == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        urlIbmCloudToken = urlIbmCloudToken.replace("xxxxxx", tenantId);
        try {
            final RestTemplate restTemplate = new RestTemplateBuilder().build();
            BodyToSend bodyOut = new BodyToSend("password", entityIn.serviceAccountName, entityIn.serviceAccountPass);
            //restTemplate.exchange (urlIbmCloudToken, HttpMethod.POST, new HttpEntity<IbmTokenResponse>(createHeaders(entityIn.serviceAccountName, entityIn.serviceAccountPass)), body);

            //String response = restTemplate.postForObject(urlIbmCloudToken, entityIn, EntityIncoming.class);

            log.info("@@@@@@@@@@@@@@@@@@@ Last line token method");
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;

    }

    public HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
        };
    }



}
