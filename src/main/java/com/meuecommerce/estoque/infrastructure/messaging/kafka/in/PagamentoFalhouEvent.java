package com.meuecommerce.estoque.infrastructure.messaging.kafka.in;

public record PagamentoFalhouEvent(
     String pedidoId,
    String sku,
    Integer quantidade
) {
    
}
