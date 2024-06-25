package com.mobigen.framework.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class NuxtSSRStaticUrlFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
            /**
             * Nuxt를 SSR 모드로 배포 시, NuxtLink 경로에 html 이 없기 때문에
             * 서버쪽에서 강제로 index.html을 추가하여 매핑한다.
             * @TODO sub-page 를 만들면 index.html로 해결이 안되기 때문에 좀더 복잡한 패틴 처리가 필요함
             * TODO: 동적 라우터 처리 필요.
             * @return
             */
            @Override
            public String getRequestURI() {
                HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
                String uri = httpRequest.getRequestURI();
                String url = httpRequest.getServletPath();

                // 리소스 파일. api 요청 제외한 화면 접근 시 index.html을 강제로 매핑
                if (!url.startsWith("/_nuxt/") && !url.contains(".")&& !url.startsWith("/api")) {
                    return uri + "/index.html";
                }
                return uri;
            }
        }, servletResponse);
    }
}
