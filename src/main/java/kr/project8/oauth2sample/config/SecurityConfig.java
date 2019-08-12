package kr.project8.oauth2sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
}
