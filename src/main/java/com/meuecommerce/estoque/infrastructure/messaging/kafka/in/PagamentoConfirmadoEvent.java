package com.meuecommerce.estoque.infrastructure.messaging.kafka.in;

public record PagamentoConfirmadoEvent(
     String pedidoId,
    String sku,
    Integer quantidade
) {
    
}
