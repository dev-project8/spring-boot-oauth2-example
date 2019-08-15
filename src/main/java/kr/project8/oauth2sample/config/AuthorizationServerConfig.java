package kr.project8.oauth2sample.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Lazy
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /**
         *  password grant type 에서 authenticationManager를 추가 하지 않으면
         *  UnsupportedGrantTypeException이 발생한다.
         */
        endpoints.authenticationManager(authenticationManager);
        /**
         *
         */
//        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("client")
                .secret("password")  // password
                .redirectUris("http://localhost:9000/callback")
                .authorizedGrantTypes("password", "authorization_code", "implicit", "client_credentials", "refresh_token")
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(240)
                .scopes("read_profile");
    }


    /**
     * 보안에 관련된 설정
     * 권한, 접근제어등은 여기서 설정한다.
     *
     * 보안이 요구되는 endpoints (기본은 denyAll() 이므로 적절히 고쳐서 사용한다)
     * 1) ~~/check_token (resource server가 rest로 token의 검증을 요청할 때 사용하는 endpoint, checkTokenAcess 로 조절)
     * 2) ~~/token_key (JWT 사용시, 토큰 검증을 위한 공개키를 노출하는 endpoint, tokenKeyAccess로 조절)
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients(); // 기본적으로 HTTP HEADER AUTH가 적용되므로, FORM 전송으로 AUTH하기 위해서 적용
//        security.checkTokenAccess("isAuthenticated()"); // ~~/check_token으로 remoteTokenService가 토큰의 해석을 의뢰할 경우, 해당 endpoint의 권한 설정(기본 denyAll())
    }
}
