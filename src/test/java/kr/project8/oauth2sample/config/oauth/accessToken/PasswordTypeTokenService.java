package kr.project8.oauth2sample.config.oauth.accessToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PasswordTypeTokenService implements TokenService {

    private final TestRestTemplate template;

    @Override
    public Token getToken(TokenRequest request) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.set("username", request.getUsername());
        parameters.set("password", request.getPassword());
        parameters.set("grant_type", "password");
        parameters.set("scope", request.getScope());

        ResponseEntity<Map> response = template
                .withBasicAuth(request.getClient(), request.getSecret())    // client
                .postForEntity("/oauth/token", parameters, Map.class);

        log.info("response => {}", response.getBody().toString());

        return Token.of((String) response.getBody().get("access_token"), (String) response.getBody().get("refresh_token"));
    }

}
