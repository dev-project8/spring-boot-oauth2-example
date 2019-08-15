package kr.project8.oauth2sample.config.oauth.accessToken;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public abstract class OwnerLoginTokenService implements TokenService {

    protected final TestRestTemplate template;

    // ResourceOwnerLogin
    // 이 호출로 스프링은 세션에 client_id, redirect_uri, response_type, scope을 저장해 놓습니다.
    /**
     * {@link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint } 참고
     */
    protected String resourceOwnerLogin(String clientId, String redirectUri, String responseType, String scope) {
        String url = "/oauth/authorize?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=" +responseType +
                "&scope=" + scope;
        ResponseEntity<String> response = template
                .withBasicAuth("user1", "pass1")
                .getForEntity(url, String.class);

        return response.getHeaders().get("Set-Cookie").get(0);  // SessionId
    }


}
