package com.meuecommerce.estoque.infrastructure.messaging.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.meuecommerce.estoque.application.ports.EstoqueEventPublisher;
import com.meuecommerce.estoque.domain.events.EstoqueBaixadoEvent;
import com.meuecommerce.estoque.domain.events.EstoqueFalhaReservaEvent;
import com.meuecommerce.estoque.domain.events.EstoqueLiberadoEvent;
import com.meuecommerce.estoque.domain.events.EstoqueReservadoEvent;

@Component
public class KafkaEstoqueEventPublisher implements EstoqueEventPublisher {

    private final KafkaTemplate<Long, Object> kafkaTemplate;

    public KafkaEstoqueEventPublisher(KafkaTemplate<Long, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void estoqueReservado(Long pedidoId) {
        kafkaTemplate.send(
            "estoque.reservado",
            pedidoId,
            new EstoqueReservadoEvent(pedidoId)
        );
    }

    @Override
    public void estoqueFalhaReserva(Long pedidoId) {
        kafkaTemplate.send(
            "estoque.falha",
            pedidoId,
            new EstoqueFalhaReservaEvent(pedidoId)
        );
    }

    @Override
    public void estoqueLiberado(Long pedidoId) {
        kafkaTemplate.send(
            "estoque.liberado",
            pedidoId,
            new EstoqueLiberadoEvent(pedidoId)
        );
    }

    @Override
    public void estoqueBaixado(Long pedidoId, String sku, Integer quantidade) {
        kafkaTemplate.send(
            "estoque.baixado",
            pedidoId,
            new EstoqueBaixadoEvent(pedidoId)
        );
    }
    
}
