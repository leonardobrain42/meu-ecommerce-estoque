package com.meuecommerce.estoque.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Configuration
public class jacksonConfig {
        
    @Bean
    @Primary // Isso diz ao Spring: "Use este aqui para TUDO (Web e Kafka)"
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Registra suporte para datas (Java 8+)
        mapper.registerModule(new JavaTimeModule());
        
        // Em vez de usar a constante que está dando erro, 
        // usamos o método de configuração direta por String ou Feature
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        return mapper;
    }
}
