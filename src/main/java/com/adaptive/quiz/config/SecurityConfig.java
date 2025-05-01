package com.adaptive.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Security is handled in this configuration class. It has whitelist which doesn't need any security,
 * and all other resources are secured with username/password set into context.
 * Spring security handles security in this project, hence this class configures the requirement to
 * tell spring security how to handle security. This reduces code, less maintenance and spring security is
 * well tested API.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Whitelist resources which doesn't need security.
     */
    private static final String[] AUTH_WHITELIST = {
            "/management/**",
            "/logout",
            "/login",
            "/signup1.html",
            "/signup/**",
            "/error",
            "/",
            "/static/**",
            "/css/**",
            "/js/**",
            "/index"
    };

    /**
     * Password encoder. We use {@link BCryptPasswordEncoder} to encode user password
     * and store it in database table. Spring security automatically applies encryption and decryption.
     *
     * @return passwordEncoder {@link BCryptPasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Datasource is configured in application.yml
     */
    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    /**
     * Important configurations for spring security for this project.
     * It whitelists few resources such that spring security will not apply security for those resources.
     * Other resources are secured. Accessing those resources requires valid username/password.
     * Spring security handles login, hence user credentials stored in application context when login.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a ->
                        a.requestMatchers(AUTH_WHITELIST)
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .headers(h -> h.frameOptions(Customizer.withDefaults()))
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/"))
                .build();
    }
}
