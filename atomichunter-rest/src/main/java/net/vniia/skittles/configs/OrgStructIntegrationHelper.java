package net.vniia.skittles.configs;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class OrgStructIntegrationHelper {
    @Value("${remoteService.user}")
    private String remoteServiceLogin;

    @Value("${remoteService.password}")
    private String remoteServicePassword;

    public HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String auth = remoteServiceLogin + ":" + remoteServicePassword;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII) );
            String authHeader = "Basic " + new String(encodedAuth);
            set( "Authorization", authHeader );
        }};
    }

}
