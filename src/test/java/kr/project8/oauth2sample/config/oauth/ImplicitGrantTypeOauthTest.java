package kr.project8.oauth2sample.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ImplicitGrantTypeOauthTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void AccessToken_발급_테스트() {
        String clientId = "client";
        String redirectUri = "http://localhost:9000/callback";
        String responseType = "token";
        String scope = "read_profile";

        String sessionId = resourceOwnerLogin(clientId, redirectUri, responseType, scope);
        String accessToken = requestAccessTokenBySessionId(sessionId);

        log.info("AccessToken => {}", accessToken);
    }

    // ResourceOwnerLogin
    // 이 호출로 스프링은 세션에 client_id, redirect_uri, response_type, scope을 저장해 놓습니다.
    /**
     * {@link org.springframework.security.oauth2.provider.endpoint.AbstractEndpoint } 참고
     */
    private String resourceOwnerLogin(String clientId, String redirectUri, String responseType, String scope) {
        String url = "/oauth/authorize?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=" +responseType +
                "&scope=" + scope;
        ResponseEntity<String> response = template
                .withBasicAuth("user1", "pass1")
                .getForEntity(url, String.class);

        return response.getHeaders().get("Set-Cookie").get(0);  // SessionId
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
        System.out.println(response);

        // response status code 302(redirect)
        String redirectUrl = response.getHeaders().get("Location").get(0);
        return UriComponentsBuilder.fromUriString(redirectUrl).build().getFragment();//.getQueryParams().get("access_token").get(0);

    }
}