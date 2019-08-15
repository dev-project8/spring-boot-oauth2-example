package kr.project8.oauth2sample.config.oauth.accessToken;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TokenRequest {

    private String client;
    private String secret;
    private String username;
    private String password;
    private String redirectUrl;
    private List<String> scopes;
    @Setter
    private String refreshToken;

    public String getScope() {
        return this.scopes.stream().collect(Collectors.joining("+"));
    }

    @Builder
    public TokenRequest(String client, String secret, String username, String password, String redirectUrl, List<String> scopes, String refreshToken) {
        this.client = client;
        this.secret = secret;
        this.username = username;
        this.password = password;
        this.redirectUrl = redirectUrl;
        this.scopes = scopes;
        this.refreshToken = refreshToken;
    }
}
