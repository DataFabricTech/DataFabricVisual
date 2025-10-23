package com.mobigen.framework.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    @Value("${spring.rabbitmq.port}")
    private Integer rabbitmqPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;

    // 1️⃣ RabbitMQ 서버 연결 설정
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    // 2️⃣ RabbitMQ Admin 객체 생성 (Queue, Exchange, Binding 관리)
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    // 3️⃣ 메시지를 보내는 템플릿 (Producer)
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    // 4️⃣ DirectExchange: 라우팅키로 메시지 전달
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct.exchange");
    }

    // 5️⃣ Queue 생성
    @Bean
    public Queue queue() {
        return new Queue("sample.queue", true); // durable=true (서버 재시작해도 유지)
    }

    // 6️⃣ Queue와 Exchange 연결 (Binding)
    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(queue)
                .to(directExchange)
                .with("sample.routingKey"); // routing key 지정
    }
}
