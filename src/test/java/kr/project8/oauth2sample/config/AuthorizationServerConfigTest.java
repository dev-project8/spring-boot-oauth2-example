package kr.project8.oauth2sample.config;

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
public class AuthorizationServerConfigTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    // Authorization Code Grant Type / AccessToken 발급 테스트
    public void test() {
        String sessionId = requestAuthorizationTokenSessionId();
        String code = requestAuthorizationCode(sessionId);

    }

    // 권한 부여 코드 요청 session획득
    // 이 호출로 스프링은 세션에 client_id, redirect_uri, response_type, scope을 저장해 놓습니다.
    public String requestAuthorizationTokenSessionId() {
        String url = "/oauth/authorize?client_id=client&redirect_uri=http://localhost:9000/callback&response_type=code&scope=read_profile";
        ResponseEntity<String> response = template
                .withBasicAuth("user1", "pass1")
                .getForEntity(url, String.class);

        for (String key : response.getHeaders().keySet()) {
            log.info("{} ===> {}", key, response.getHeaders().get(key));
        }

        return response.getHeaders().get("Set-Cookie").get(0);

    }

    // 권한 코드를 가져온다.
    public String requestAuthorizationCode(String sessionId) {
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
        for (String key : response.getHeaders().keySet()) {
            log.info("{} ===> {}", key, response.getHeaders().get(key));
        }
        String redirectUrl = response.getHeaders().get("Location").get(0);
        return UriComponentsBuilder.fromUriString(redirectUrl).build().getQueryParams().get("code").get(0);

    }
}