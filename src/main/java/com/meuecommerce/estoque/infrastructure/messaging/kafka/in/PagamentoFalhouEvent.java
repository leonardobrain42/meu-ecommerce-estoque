package com.meuecommerce.estoque.infrastructure.messaging.kafka.in;

public record PagamentoFalhouEvent(
    Long pedidoId,
    String sku,
    Integer quantidade
) {
    
}
