package kr.project8.oauth2sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Exclude H2 Console
        http.authorizeRequests()
                .antMatchers(
                        "/h2-console/**"
                ).permitAll().and()
                .headers().frameOptions().disable();

        //@formatter:off
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .formLogin().disable()
                .httpBasic();
        //@formatter:on
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * Password Grant Type시 아래 메소드(AuthenticationManager 정의) 구현하지 않으면
     * Handler dispatch failed; nested exception is java.lang.StackOverflowError 가 발생한다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("pass").roles("USER").and()
                .withUser("user1").password("pass1").roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
