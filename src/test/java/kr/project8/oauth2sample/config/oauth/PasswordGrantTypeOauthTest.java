package kr.project8.oauth2sample.config.oauth;


import kr.project8.oauth2sample.config.oauth.accessToken.PasswordTypeTokenService;
import kr.project8.oauth2sample.config.oauth.accessToken.Token;
import kr.project8.oauth2sample.config.oauth.accessToken.TokenRequest;
import kr.project8.oauth2sample.config.oauth.accessToken.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Implicit Grant Type 방식 테스트
 *
 * curl -X POST \
 *   http://localhost:8080/oauth/token \
 *   -H 'Authorization: Basic Y2xpZW50OnBhc3N3b3Jk' \
 *   -H 'Content-Type: application/x-www-form-urlencoded' \
 *   -d 'username=user&password=pass&grant_type=password&scope=read_profile'
 */
/**
 * {@link org.springframework.security.oauth2.provider.endpoint.TokenEndpoint } 참고
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordGrantTypeOauthTest {

    public static final List<String> SCOPES = Arrays.asList("read_profile") ;

    public static final String CLIENT_ID = "client";
    public static final String SECRET = "password";
    public static final String USERNAME = "user1";
    public static final String PASSWORD = "pass1";

    @Autowired
    private TestRestTemplate template;

    private TokenRequest tokenRequest;
    private TokenService tokenService;

    @Before
    public void setup() {
        this.tokenService = new PasswordTypeTokenService(template);
        this.tokenRequest = TokenRequest.builder()
                .client(CLIENT_ID)
                .secret(SECRET)
                .username(USERNAME)
                .password(PASSWORD)
                .scopes(SCOPES)
                .build();
    }

    @Test
    public void AccessToken_발급_테스트() {
        Token token = tokenService.getToken(tokenRequest);

        log.info("Token => {}", token.toString());

        Assertions.assertThat(token.getAccessToken()).isNotNull();
    }
}
