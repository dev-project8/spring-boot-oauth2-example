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
public class RefreshTokenTypeTokenService implements TokenService {

    private final TestRestTemplate template;

    @Override
    public Token getToken(TokenRequest request) {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.set("grant_type", "refresh_token");
        parameters.set("scope", request.getScope());
        parameters.set("refresh_token", request.getRefreshToken());

        ResponseEntity<Map> response = template
                .withBasicAuth(request.getClient(), request.getSecret())    // client
                .postForEntity("/oauth/token", parameters, Map.class);

        log.info("response => {}", response.getBody().toString());

        String accessToken = (String) response.getBody().get("access_token");
        return Token.of(accessToken, "");
    }

}
