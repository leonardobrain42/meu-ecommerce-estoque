package com.meuecommerce.estoque.infrastructure.messaging.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.meuecommerce.estoque.application.ports.EstoqueEventPublisher;
import com.meuecommerce.estoque.domain.events.EstoqueBaixadoEvent;
import com.meuecommerce.estoque.domain.events.EstoqueLiberadoEvent;
import com.meuecommerce.estoque.domain.events.EstoqueReservadoEvent;

@Component
public class KafkaEstoqueEventPublisher implements EstoqueEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEstoqueEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void estoqueReservado(String pedidoId, String sku, Integer quantidade) {
        kafkaTemplate.send(
            "estoque.reservado",
            pedidoId,
            new EstoqueReservadoEvent(pedidoId, sku, quantidade)
        );
    }

    @Override
    public void estoqueLiberado(String pedidoId, String sku, Integer quantidade) {
        kafkaTemplate.send(
            "estoque.liberado",
            pedidoId,
            new EstoqueLiberadoEvent(pedidoId, sku, quantidade)
        );
    }

    @Override
    public void estoqueBaixado(String pedidoId, String sku, Integer quantidade) {
        kafkaTemplate.send(
            "estoque.baixado",
            pedidoId,
            new EstoqueBaixadoEvent(pedidoId, sku, quantidade)
        );
    }
    
}
