package com.mobigen.framework.configuration;

import jakarta.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.valves.ValveBase;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Value("${http-client-configuration.max-total}")
    private int maxTotal;

    @Value("${http-client-configuration.default-max-per-route}")
    private int defaultMaxPerRoute;

    @Value("${http-client-configuration.default-keep-alive-duration}")
    private int defaultKeepAliveDuration;

    @Value("${http-client-configuration.connection-reqeust-timeout}")
    private int connectionRequestTimeout;

    @Value("${http-client-configuration.connection-timeout}")
    private int connectionTimeout;

    @Value("${http-client-configuration.socket-timeout}")
    private int socketTimeout;

    @Value("${http-client-configuration.idle-timeout}")
    private int idleTimeout;

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                //((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
    }
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers((context) -> {
            if (context.getParent() instanceof StandardHost parent) {
                parent.setErrorReportValveClass(CustomErrorReportValve.class.getName());
                parent.addValve(new CustomErrorReportValve());
            }
        });

        factory.addConnectorCustomizers(connector -> {
            if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
                AbstractHttp11Protocol<?> protocolHandler = (AbstractHttp11Protocol<?>) connector
                    .getProtocolHandler();
                protocolHandler.setKeepAliveTimeout(defaultKeepAliveDuration);
                protocolHandler.setMaxKeepAliveRequests(maxTotal);
                protocolHandler.setUseKeepAliveResponseHeader(true);
                protocolHandler.setRelaxedPathChars("<>[\\\\]^`{|}");
                protocolHandler.setRelaxedQueryChars("<>[\\\\]^`{|}");
                protocolHandler.setConnectionTimeout(connectionTimeout);
            }
        });

    }


}