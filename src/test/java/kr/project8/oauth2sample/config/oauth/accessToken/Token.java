package kr.project8.oauth2sample.config.oauth.accessToken;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Token {

    private String accessToken;
    private String refreshToken;

    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Token of(String accessToken, String refreshToken) {
        return new Token(accessToken, refreshToken);
    }
}
