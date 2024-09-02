package com.mobigen.framework.filter;

import com.mobigen.framework.interceptor.CustomHttpServletRequestWrapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@AllArgsConstructor
@Order(2)
public class QueryParamFilter implements Filter {

    /**
     * 일부 특수 문자의 경우 \특수문자 처리를 해줘야 함. open meta 에 \ 가 붙어서 가는 특수 문자를 처리함.
     * ^
     * ()
     * {}
     * []
     * !
     * "
     */
    private static final String SPECIAL_CHARS_PATTERN = "([\\^(){}\\[\\]\"/!\\+\\-])";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String q = httpRequest.getParameter("q");

        if (q != null) {
            Pattern pattern = Pattern.compile(SPECIAL_CHARS_PATTERN);
            Matcher matcher = pattern.matcher(q);
            String modifiedQ = matcher.replaceAll("\\\\$1");

            HttpServletRequest wrappedRequest = new CustomHttpServletRequestWrapper(httpRequest, "q", modifiedQ);
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}