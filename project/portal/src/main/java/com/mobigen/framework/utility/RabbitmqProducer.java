package com.mobigen.framework.utility;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RabbitmqProducer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;

    public RabbitmqProducer(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
    }

    public void publishMetadataTask(String queueName, Map<String, Object> metadata) {
        // Queue 존재하지 않으면 생성
        Queue queue = new Queue(queueName, true);
        amqpAdmin.declareQueue(queue);

        try {
            // Map → JSON 문자열 변환
            String json = objectMapper.writeValueAsString(metadata);

            Message message = MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setContentEncoding("UTF-8")
                    .build();

            // Default Exchange ("") 사용 → queueName을 routing key로 지정
            rabbitTemplate.send("", queueName, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish metadata", e);
        }
    }
}
