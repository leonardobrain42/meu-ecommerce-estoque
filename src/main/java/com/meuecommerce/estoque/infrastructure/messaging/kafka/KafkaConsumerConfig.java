package com.meuecommerce.estoque.infrastructure.messaging.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.JacksonJsonMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerContainerFactory(
        ConsumerFactory<String, Object> consumerFactory,
        ObjectMapper objectMapper
    ) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        factory.setRecordMessageConverter(
            new JacksonJsonMessageConverter()
        );
        factory.setConcurrency(3); // paralelismo

        return factory;
    }
}