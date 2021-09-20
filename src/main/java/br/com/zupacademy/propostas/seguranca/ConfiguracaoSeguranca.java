package br.com.zupacademy.propostas.seguranca;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http    .cors().and().csrf().disable()
                .authorizeRequests(authorizeRequest ->
                        authorizeRequest
                                .antMatchers("/**").hasAuthority("SCOPE_Teste-Scope")
                                .antMatchers("/h2-console").permitAll()
                                .antMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
