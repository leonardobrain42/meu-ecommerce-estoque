package com.meuecommerce.estoque.infrastructure.messaging.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.meuecommerce.estoque.application.EstoqueService;
import com.meuecommerce.estoque.infrastructure.messaging.kafka.in.PagamentoConfirmadoEvent;
import com.meuecommerce.estoque.infrastructure.messaging.kafka.in.PagamentoFalhouEvent;
import com.meuecommerce.estoque.infrastructure.messaging.kafka.in.PedidoCriadoEvent;

@Component
public class EstoqueEventListener {
    
    private final EstoqueService service;

    public EstoqueEventListener(EstoqueService service) {
        this.service = service;
    }

    @KafkaListener(topics = "pedido-criado", groupId = "estoque-service")
    public void onPedidoCriado(PedidoCriadoEvent event) {
        service.reservarEstoque(
            event.pedidoId(),
            event.sku(),
            event.quantidade()
        );
    }

    @KafkaListener(topics = "pagamento-confirmado", groupId = "estoque-service")
    public void onPagamentoConfirmado(PagamentoConfirmadoEvent event) {
        service.confirmarReserva(
            event.pedidoId(),
            event.sku(),
            event.quantidade());
    }

    @KafkaListener(topics = "pagamento-falhou", groupId = "estoque-service")
    public void onPagamentoFalhou(PagamentoFalhouEvent event) {
        service.liberarReserva(
            event.pedidoId(),
            event.sku(),
            event.quantidade());
    }
}
