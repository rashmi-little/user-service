package com.mindfire.backend.kafka;

import com.mindfire.backend.dto.response.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserKafkaProducer {

    private final NewTopic topic;

    private final KafkaTemplate<String, UserRegistrationEvent> kafkaTemplate;

    public void sendMessage(UserRegistrationEvent event) {
        log.info("String format => user registration event => %s", event.toString());

        Message<UserRegistrationEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);
    }

}
