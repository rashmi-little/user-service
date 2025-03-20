package com.mindfire.backend.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.mindfire.basedomains.dto.UserRegistrationEvent;

@Service
@RequiredArgsConstructor
public class UserKafkaProducer {

	private final NewTopic topic;

	private final KafkaTemplate<String, UserRegistrationEvent> kafkaTemplate;

	/**
	 * Publishes a UserRegistrationEvent to the configured Kafka topic. This method
	 * converts the event into a Kafka message and sends it.
	 *
	 * @param event the user registration event to be published
	 */
	public void publishUserRegistrationEvent(UserRegistrationEvent event) {
		Message<UserRegistrationEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();
		kafkaTemplate.send(message);
	}

}
