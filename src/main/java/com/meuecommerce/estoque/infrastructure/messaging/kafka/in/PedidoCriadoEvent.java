package com.meuecommerce.estoque.infrastructure.messaging.kafka.in;

public record PedidoCriadoEvent(
    String pedidoId,
    String sku,
    Integer quantidade
) {
    
}
