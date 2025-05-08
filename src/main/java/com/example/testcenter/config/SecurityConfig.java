package com.example.testcenter.config;

import com.example.testcenter.security.JwtTokenFilter;
import com.example.testcenter.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor (onConstructor_ ={@Lazy})
public class SecurityConfig {

    private final ApplicationContext applicationContext;  //?
    private final JwtTokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final AntPathRequestMatcher[] SWAGGER_ENDPOINT = {
            new AntPathRequestMatcher("/**swagger**/**"),
            new AntPathRequestMatcher( "/swagger-resources"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/v2/api-docs"),
            new AntPathRequestMatcher("/v3/api-docs/**")
    };


    /*
        @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }
     */

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {  //проверяет успешна аутентификация или нет
        return configuration.getAuthenticationManager();
    }


    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {  // настройка
        httpSecurity
                .csrf().disable()
                .cors()
                .and()
             //   .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Unauthorized.");
                }))
                .accessDeniedHandler(((request, response, authException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Unauthorized");
                }))
                .and()
                .authorizeHttpRequests(auth -> auth
                      //  .requestMatchers(new AntPathRequestMatcher("/**swagger**/**")).permitAll()
                        //  .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(SWAGGER_ENDPOINT).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/orders/checkStatus")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/orders/**")).hasRole("MANAGER")
                        .anyRequest().authenticated()
                        .and())
                        .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}