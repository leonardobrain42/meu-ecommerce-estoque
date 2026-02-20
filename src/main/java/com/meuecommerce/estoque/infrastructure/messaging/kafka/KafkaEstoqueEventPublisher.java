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

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEstoqueEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void estoqueReservado(EstoqueReservadoEvent event) {
        kafkaTemplate.send(
            "estoque.reservado",
            event.getPedidoId().toString(),
            event
        );
    }

    @Override
    public void estoqueFalhaReserva(EstoqueFalhaReservaEvent event) {
        kafkaTemplate.send(
            "estoque.falha",
            event.getPedidoId().toString(),
            event
        );
    }

    @Override
    public void estoqueLiberado(EstoqueLiberadoEvent event) {
        kafkaTemplate.send(
            "estoque.liberado",
            event.getPedidoId().toString(),
            event
        );
    }

    @Override
    public void estoqueBaixado(EstoqueBaixadoEvent event) {
        kafkaTemplate.send(
            "estoque.baixado",
            event.getPedidoId().toString(),
            event
        );
    }
    
}
