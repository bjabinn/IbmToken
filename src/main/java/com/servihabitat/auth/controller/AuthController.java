package com.servihabitat.auth.controller;

import com.servihabitat.auth.model.BodyToSend;
import com.servihabitat.auth.model.EntityIncoming;
import com.servihabitat.auth.model.IbmTokenResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.jmx.export.UnableToRegisterMBeanException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

        log.info("@@@@@@@@@@@@@@@@@@@ First line /token method");

        if (entityIn.getClientId() == "" || entityIn.getSecret() == null ||
                entityIn.getServiceAccountName() == "" || entityIn.getServiceAccountPass() == "")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        urlIbmCloudToken = urlIbmCloudToken.replace("xxxxxx", tenantId);
        try {
            String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((entityIn.getClientId() + ":" + entityIn.getSecret()).getBytes());

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            requestHeaders.add("Authorization", authorizationHeader);

            BodyToSend bodyToSend = new BodyToSend("password",
                    entityIn.getServiceAccountName(), entityIn.getServiceAccountPass());

            HttpEntity<BodyToSend> requestEntity = new HttpEntity<>(bodyToSend, requestHeaders);

            RestTemplate restTemplate = new RestTemplateBuilder().build();
            ResponseEntity<IbmTokenResponse> responseEntity = restTemplate.exchange(
                    urlIbmCloudToken,
                    HttpMethod.POST,
                    requestEntity,
                    IbmTokenResponse.class);

            IbmTokenResponse response = new IbmTokenResponse();
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response = responseEntity.getBody();
            }

            log.debug("@@@@@@@@@@@@@@@@@@@ User: " + entityIn.getServiceAccountName() + ", accessToken: " + response.getAccess_token());
            log.info("@@@@@@@@@@@@@@@@@@@ Last line /token method. Everthing was fine.");
            return ResponseEntity.ok(responseEntity.getBody());
        } catch (HttpClientErrorException.BadRequest ex){
            log.error("@@@@@@@@@@@@@@@@@@@ Bad Request Exception: " + ex.getMessage());
            return ResponseEntity.badRequest().body("Bad Request: " + ex.getMessage());
        } catch (HttpClientErrorException.Unauthorized ex) {
            log.error("@@@@@@@@@@@@@@@@@@@ Unauthorized Exception: " + ex.getMessage());
            return ResponseEntity.status(401).body("Unauthorized: " + ex.getMessage());
        } catch (Exception ex){
            log.error("@@@@@@@@@@@@@@@@@@@ Exception: " + ex.getMessage());
            return ResponseEntity.status(500).body("EXCEPTION: " + ex.getMessage());
        }


    } //end of gettingAccessToken
} //end of class
