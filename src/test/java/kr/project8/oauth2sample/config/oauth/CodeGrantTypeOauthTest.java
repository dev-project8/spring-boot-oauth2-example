package kr.project8.oauth2sample.config.oauth;

import kr.project8.oauth2sample.config.oauth.accessToken.*;
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
 * Authorization Code Grant Type 방식 테스트
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CodeGrantTypeOauthTest {

    public static final String REDIRECT_URI = "http://localhost:9000/callback";
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
        this.tokenService = new CodeGrantTypeTokenService(template);
        this.tokenRequest = TokenRequest.builder()
                .client(CLIENT_ID)
                .secret(SECRET)
                .username(USERNAME)
                .password(PASSWORD)
                .redirectUrl(REDIRECT_URI)
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
