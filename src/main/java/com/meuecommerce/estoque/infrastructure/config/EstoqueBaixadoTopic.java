package com.meuecommerce.estoque.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class EstoqueBaixadoTopic {
    
    @Bean
    public NewTopic estoqueBaixadTopic() {
        return TopicBuilder.name("estoque.reservado") // Nome EXATO do tópico
                .partitions(1)                // Número de partições (bom para performance)
                .replicas(1)                  // Número de réplicas (1 se estiver em dev/local)
                .build();
    }
}
