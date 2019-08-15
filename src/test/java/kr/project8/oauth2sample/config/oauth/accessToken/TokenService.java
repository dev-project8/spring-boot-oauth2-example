package kr.project8.oauth2sample.config.oauth.accessToken;

public interface TokenService {

    Token getToken(TokenRequest tokenRequest);
}
