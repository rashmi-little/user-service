package com.mindfire.backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
	@Value("${spring.kafka.topic.name}")
	private String topicName;

	/**
	 * Creates and configures a Kafka topic.
	 * 
	 * This method initializes a new Kafka topic with the name specified in the
	 * application properties. The topic is registered as a Spring Bean to be
	 * managed by the application context.
	 *
	 * @return a NewTopic instance representing the Kafka topic
	 */
	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(topicName).build();
	}
}
