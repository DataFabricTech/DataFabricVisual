package com.mobigen.framework.service.iris;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.Token;
import com.mobigen.framework.iris.User;
import com.mobigen.framework.security.SessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IRISService {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private Token tokenAPI;

    @Autowired
    private ResourceLoader loader;

    @Autowired
    private IRISProperties properties;

    private Map<String, String> getRouteByLocale(String locale) throws Exception {
        String fileName = String.format("classpath:/i18n/route/route_%s.yml", locale);
        InputStream stream = null;
        Map<String, String> yMap = null;

        try {
            Yaml y = new Yaml();
            Resource resource = loader.getResource(fileName);
            stream = resource.getInputStream();
            yMap = y.load(stream);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            stream.close();
        }

        return yMap;
    }

    public Object getRoute(String locale) throws Exception {
        if (!locale.isEmpty()) {
            return getRouteByLocale(locale);
        }

        Map<String, Object> route = new HashMap<String, Object>();
        String[] locales = properties.getRoute().getLocales();
        for (int i = 0; i < locales.length; i++) {
            String l = locales[i];
            Map<String, String> r = getRouteByLocale(l);
            route.put(l, r);
        }

        return route;
    }

    /**
     * Brick X-Access-Token 과 Angora Token 생명 연장
     * 
     * @return
     * @throws Exception
     */
    // public void heartbeat(HttpServletRequest request) throws Exception {
    //     // 세션 없음
    //     if (null == sessionManager.getUser()) {
    //         return;
    //     }

    //     // brick
    //     User user = sessionManager.getUser();
    //     String xAccessToken = user.getXAccessToken();
    //     xAccessToken = tokenAPI.isValidBrickToken(xAccessToken);
    //     if (null == xAccessToken) {
    //         throw new Exception("Brick Token Invalid");
    //     }
    //     user.setXAccessToken(xAccessToken);

    //     // // angora
    //     // String angoraToken = (String)
    //     // sessionManager.getSessionData(RestApiCookieHolder.HEADER_AUTHORIZATION);
    //     // try {
    //     // if (!tokenAPI.isValidAngoraToken(angoraToken)) {
    //     // angoraToken = tokenAPI.getAngoraToken(xAccessToken);
    //     // sessionManager.addSessionData(RestApiCookieHolder.HEADER_AUTHORIZATION,
    //     // angoraToken);
    //     // log.info("ANGORA Token Refresh: " + angoraToken);
    //     // }
    //     // } catch (Exception e) {
    //     // angoraToken = tokenAPI.getAngoraToken(xAccessToken);
    //     // sessionManager.addSessionData(RestApiCookieHolder.HEADER_AUTHORIZATION,
    //     // angoraToken);
    //     // log.info("[in Catch]ANGORA Token Refresh: " + angoraToken);
    //     // }

    //     log.info("Heartbeat [" + request.getSession().getId() + "] : " + xAccessToken);
    // }
}
