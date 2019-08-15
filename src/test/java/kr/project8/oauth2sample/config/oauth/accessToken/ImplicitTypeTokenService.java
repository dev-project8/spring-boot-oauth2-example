package kr.project8.oauth2sample.config.oauth.accessToken;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * ImplicitType은 Refresh Token을 발행하지 않는다
 */
@Slf4j
public class ImplicitTypeTokenService extends OwnerLoginTokenService {

    public ImplicitTypeTokenService(TestRestTemplate template) {
        super(template);
    }

    @Override
    public Token getToken(TokenRequest request) {
        String sessionId = super.resourceOwnerLogin(request.getClient(), request.getRedirectUrl(), "token", request.getScope());
        String accessToken = requestAccessTokenBySessionId(sessionId);
        return Token.of(accessToken, null);
    }

    // 직접 AccessToken를 가져온다.
    private String requestAccessTokenBySessionId(String sessionId) {
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
        log.info("redirectUrl => {}", redirectUrl);
        return UriComponentsBuilder.fromUriString(redirectUrl).build().getFragment();//.getQueryParams().get("access_token").get(0);

    }
}
