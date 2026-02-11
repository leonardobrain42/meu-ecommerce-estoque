package com.meuecommerce.estoque.infrastructure.messaging.kafka.in;

public record PagamentoConfirmadoEvent(
    Long pedidoId,
    String sku,
    Integer quantidade
) {
    
}
