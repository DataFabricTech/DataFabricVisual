// package com.mobigen.framework.configuration;

// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
// import java.util.concurrent.TimeUnit;

// import com.mobigen.framework.component.Messages;

// import org.apache.http.HeaderElement;
// import org.apache.http.HeaderElementIterator;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.CookieStore;
// import org.apache.http.client.config.RequestConfig;
// import org.apache.http.conn.ConnectionKeepAliveStrategy;
// import org.apache.http.cookie.Cookie;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
// import org.apache.http.message.BasicHeaderElementIterator;
// import org.apache.http.protocol.HTTP;
// import org.apache.http.protocol.HttpContext;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.web.client.RestTemplateBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.MediaType;
// import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
// import org.springframework.http.converter.HttpMessageConverter;
// import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
// import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.web.client.RestTemplate;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Configuration
// @EnableScheduling
// public class HttpClientConfiguration {
//     @Autowired
//     private Messages message;

//     @Value("${http-client-configuration.max-total}")
//     private int maxTotal = 0;

//     @Value("${http-client-configuration.default-max-per-route}")
//     private int defaultMaxPerRoute = 0;

//     @Value("${http-client-configuration.default-keep-alive-duration}")
//     private long defaultKeepAliveDuration = 0;

//     @Value("${http-client-configuration.connection-reqeust-timeout}")
//     private int connectionRequestTimeout = 0;

//     @Value("${http-client-configuration.connection-timeout}")
//     private int connectionTimeout = 0;

//     @Value("${http-client-configuration.socket-timeout}")
//     private int socketTimeout = 0;

//     @Value("${http-client-configuration.idle-timeout}")
//     private int idleTimeout = 0;

//     @Bean
//     public PoolingHttpClientConnectionManager pollingConnectionManager() {
//         PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//         connectionManager.setMaxTotal(this.maxTotal);
//         connectionManager.setDefaultMaxPerRoute(this.defaultMaxPerRoute);

//         return connectionManager;
//     }

//     @Bean
//     public ConnectionKeepAliveStrategy keepAliveStrategy() {
//         return new ConnectionKeepAliveStrategy() {
//             @Override
//             public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
//                 HeaderElementIterator it = new BasicHeaderElementIterator(
//                         response.headerIterator(HTTP.CONN_KEEP_ALIVE));
//                 while (it.hasNext()) {
//                     HeaderElement he = it.nextElement();
//                     String param = he.getName();
//                     String value = he.getValue();
//                     if (value != null && param.equalsIgnoreCase("timeout")) {
//                         return Long.parseLong(value) * 1000;
//                     }
//                 }
//                 return defaultKeepAliveDuration;
//             }
//         };
//     }

//     @Bean
//     public CookieStore cookieStore() {
//         return new CookieStore() {
//             private List<Cookie> __list = new ArrayList<Cookie>();

//             @Override
//             public List<Cookie> getCookies() {
//                 return this.__list;
//             }

//             @Override
//             public boolean clearExpired(Date date) {
//                 return false;
//             }

//             @Override
//             public void clear() {
//             }

//             @Override
//             public void addCookie(Cookie cookie) {
//             }
//         };
//     }

//     @Bean
//     public RequestConfig requestConfig() {
//         return RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
//                 .setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
//     }

//     @Bean
//     public CloseableHttpClient httpClient() {
//         return HttpClients.custom().setKeepAliveStrategy(this.keepAliveStrategy())
//                 .setConnectionManager(this.pollingConnectionManager()).setDefaultRequestConfig(this.requestConfig())
//                 .setDefaultCookieStore(this.cookieStore()).build();
//     }

//     @Bean
//     public RestTemplate restTemplate(RestTemplateBuilder builder) {
//         HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//         requestFactory.setHttpClient(this.httpClient());

//         List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//         MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//         List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
//         supportedMediaTypes.add(MediaType.APPLICATION_JSON);
//         supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
//         supportedMediaTypes.add(MediaType.TEXT_HTML);

//         converter.setSupportedMediaTypes(supportedMediaTypes);
//         messageConverters.add(converter);

//         RestTemplate restTemplate = new RestTemplate(requestFactory);
//         restTemplate.setMessageConverters(messageConverters);

//         return restTemplate;
//     }

//     @Bean
//     public Runnable idleConnectionMonitor(final PoolingHttpClientConnectionManager connectionManager) {
//         return new Runnable() {
//             @Override
//             @Scheduled(fixedDelay = 100000)
//             public void run() {
//                 try {
//                     if (null == connectionManager) {
//                         return;
//                     }
//                     connectionManager.closeExpiredConnections();
//                     connectionManager.closeIdleConnections(idleTimeout, TimeUnit.SECONDS);
//                     log.info(message.get("HttpClientConfiguration.idleConnectionMonitor") + "={}",
//                             connectionManager.getTotalStats().getAvailable());
//                 } catch (Exception e) {
//                     log.error(message.get("com.mobigen.framework.configuration.HttpClientConfiguration") + "={}, e={}",
//                             e.getMessage(), e);
//                 }
//             }
//         };
//     }
// }