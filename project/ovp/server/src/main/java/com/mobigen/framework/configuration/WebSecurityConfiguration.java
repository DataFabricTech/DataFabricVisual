package com.mobigen.framework.configuration;

import com.mobigen.framework.utility.FrameworkProperties;
import com.mobigen.framework.utility.Token;
import com.mobigen.framework.security.JwtAuthenticationEntryPoint;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final Token token;


    private final FrameworkProperties properties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(properties.getSecurity().getIgnores());
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http.cors((cors) -> cors.configurationSource(corsConfigurationSource()));

        // 토큰 방식 활용을 위해 Session Stateless 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Set Logout
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl(properties.getSecurity().getRedirectUrl())
                .addLogoutHandler((request, response, authentication) -> {
                    for (Cookie cookie : request.getCookies()) {
                        String cookieName = cookie.getName();
                        if (properties.getToken().getAccessToken().equals(cookieName)) {
                            // OVP에서 Keycloak을 사용하지 않음
                            // TODO: Open Metadata 사용자 로그아웃 처리 필요
                            // Keycloak Token Expire
//                            Map<String, Object> param = token.getTokensByRequest(request);
//                            keycloakAPI.logout(param);

                            // 쿠키에서 토큰값 삭제
                            Cookie cookieToDelete = new Cookie(cookieName, null);
                            cookieToDelete.setMaxAge(-1);
                            response.addCookie(cookieToDelete);
                        }
                    }
                })
        );

        // Set CSRF
        if (properties.getSecurity().getCsrf()) {
            http.csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/api/csrf-token"));
        } else {
            http.csrf(AbstractHttpConfigurer::disable);
        }

        // Set unauthorized requests exception handler
        http.exceptionHandling((exception) -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        // Set permissions on endpoints
        http.authorizeHttpRequests((auth) -> auth.requestMatchers(properties.getSecurity().getPermitAlls())
                .permitAll()
                .anyRequest()
                .authenticated());

        // set iframe option
        switch (properties.getSecurity().getIframeOption()) {
            case "same-origin" ->
                http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
            case "deny" ->
                http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny));
            case "disable" ->
                http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        }

        // add JWT token filter
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
