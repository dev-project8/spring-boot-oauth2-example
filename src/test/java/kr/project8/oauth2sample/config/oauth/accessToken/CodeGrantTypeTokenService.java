package kr.project8.oauth2sample.config.oauth.accessToken;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
public class CodeGrantTypeTokenService extends OwnerLoginTokenService {

    public CodeGrantTypeTokenService(TestRestTemplate template) {
        super(template);
    }

    @Override
    public Token getToken(TokenRequest request) {
        String sessionId = super.resourceOwnerLogin(request.getClient(), request.getRedirectUrl(), "code", request.getScope());
        String code = requestAuthorizationCode(sessionId);
        Map response = requestToken(code);

        return Token.of((String) response.get("access_token"), (String) response.get("refresh_token"));
    }

    // 권한 코드를 가져온다.
    private String requestAuthorizationCode(String sessionId) {
        String url = "/oauth/authorize";

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.set("scope.read_profile", "true");
        parameters.set("user_oauth_approval", "true");
        parameters.set("authorize", "Authorize");

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", sessionId);

        //Create a new HttpEntity
        final HttpEntity entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = template
                .withBasicAuth("user1", "pass1")
                .postForEntity(url, entity, String.class);

        // response status code 302(redirect)
        String redirectUrl = response.getHeaders().get("Location").get(0);
        return UriComponentsBuilder.fromUriString(redirectUrl).build().getQueryParams().get("code").get(0);

    }

    // 코드로 AccessToken을 요청한다.
    private Map requestToken(String code) {
        String url = "/oauth/token";
        log.info("code => {}", code);
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.set("code", code);
        parameters.set("grant_type", "authorization_code");
        parameters.set("scope", "read_profile");
        parameters.set("redirect_uri", "http://localhost:9000/callback");

        ResponseEntity<Map> response = template
                .withBasicAuth("client", "password")    // client 계정
                .postForEntity(url, parameters, Map.class);

        log.info("response => {}", response.getBody().toString());

        return response.getBody();
    }
}
