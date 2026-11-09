package com.mobigen.framework.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

@Order(2)
@Component
@Slf4j
public class NuxtSSRStaticUrlFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
                /**
                 * Nuxt를 SSR 모드로 배포 시, NuxtLink 경로에 html 이 없기 때문에
                 * 서버쪽에서 강제로 index.html을 추가하여 매핑한다.
                 * @TODO sub-page 를 만들면 index.html로 해결이 안되기 때문에 좀더 복잡한 패틴 처리가 필요함
                 * @return
                 */
                @Override
                public String getRequestURI() {
                    String url = ((HttpServletRequest) servletRequest).getRequestURI();

                    String searchHtmlFile = "static" + url + "/index.html";
                    if (url.equals("/")) {
                        searchHtmlFile = "static" + url + "index.html";
                    }

                    ClassPathResource resource = new ClassPathResource(searchHtmlFile);
                    if (resource.exists()) {
                        return url + "/index.html";
                    }
                    return url;
                }
            }, servletResponse);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
