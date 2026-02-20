package com.meuecommerce.estoque.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JacksonJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
public class KafkaConfig {

    @Bean
    public RecordMessageConverter converter() {
        // Isso ensina o @KafkaListener a converter o JSON do payload 
        // diretamente para o seu objeto Java/Record
        return new JacksonJsonMessageConverter();
    }
}