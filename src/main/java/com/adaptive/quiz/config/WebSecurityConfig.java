package com.adaptive.quiz.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http.authorizeHttpRequests(
////                        r -> r.requestMatchers("/", "/home", "/register", "helloWorld").permitAll()
////                                //.anyRequest().authenticated()
////                );
////                .formLogin(f -> f.loginPage("/login").permitAll())
////                .logout((logout) -> logout.permitAll());
//
//        http.authorizeRequests().anyRequest().permitAll();
//        return http.build();
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails details = User.withDefaultPasswordEncoder()
//                .username("siva")
//                .password("password")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(details);
//    }
}
