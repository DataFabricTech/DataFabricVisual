package com.mobigen.framework.configuration;

import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.Token;
import com.mobigen.framework.security.JwtAuthenticationEntryPoint;
import com.mobigen.framework.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Value("${server.servlet.session.cookie.name}")
    private String sessionCookieName;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private Token token;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private IRISProperties properties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http = http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // http = http.cors(cors -> cors.disable());

        // Set CSRF
        if (properties.getSecurity().getCsrf()) {
            http = http.csrf(csrf -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/**", "/api/csrf-token")); // /**는 테스트를위해 넣어놓음 나중에 로그인 부분 구현했을시 제거 필요
        } else {
            http = http.csrf(csrf -> csrf.disable());
        }

        // Set unauthorized requests exception handler
        http = http.exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        
        // Set perimssions on endpoints
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(properties.getSecurity().getPermitAlls()).permitAll()
                .requestMatchers("/admin/data/*", "/api/admin/data/*").hasAuthority("Manager")
                .requestMatchers("/admin/manager/*", "/api/admin/manager/*").hasAuthority("Admin")
                .requestMatchers("/admin/*", "/api/admin/*").hasAuthority("Super-Admin")
                .requestMatchers("/**", "/api/**").hasAuthority("User")
                .anyRequest()
                .authenticated());

        // Set logout
        http = http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl(properties.getSecurity().getRedirectUrl())
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .addLogoutHandler((request, response, auth) -> {
                    // Access Token 쿠키 삭제
                    Cookie cookieToDeleteAccessToken = new Cookie(properties.getAccessToken().getName(), null);
                    cookieToDeleteAccessToken.setMaxAge(-1);
                    response.addCookie(cookieToDeleteAccessToken);

                    // Refresh Token 쿠키 삭제
                    Cookie cookieToDeleteRefreshToken = new Cookie(properties.getRefreshToken().getName(), null);
                    cookieToDeleteRefreshToken.setMaxAge(-1);
                    response.addCookie(cookieToDeleteRefreshToken);
                }));

        // set iframe option
        switch (properties.getSecurity().getIframeOption()) {
            case "same-origin":
                http = http.headers(headers -> headers.frameOptions(fo -> fo.sameOrigin()));
                break;
            case "deny":
                http = http.headers(headers -> headers.frameOptions(fo -> fo.deny()));
                break;
            case "disable":
                http = http.headers(headers -> headers.frameOptions(fo -> fo.disable()));
                break;
            default:
                break;
        }

        // add JWT token filter
        http.with(new JWTSecurityConfiguration(token, sessionManager, properties), customizer -> customizer.getClass());
        
        // allow one session
        http = http.sessionManagement((session) -> session
                .maximumSessions(1) // 최대 허용 세션 수
                .maxSessionsPreventsLogin(false) // 기존 세션 강제 만료 처리
                .expiredUrl("/")); // 리다이렉트 경로

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(properties.getSecurity().getCors().getAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList(properties.getSecurity().getCors().getAllowedMethods()));
        configuration.setAllowedHeaders(Arrays.asList(properties.getSecurity().getCors().getAllowedHeaders()));
        configuration.setAllowCredentials(properties.getSecurity().getCors().getAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(properties.getSecurity().getCors().getPattern(), configuration);

        return source;
    }
}