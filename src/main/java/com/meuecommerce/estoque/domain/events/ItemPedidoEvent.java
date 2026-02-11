package com.meuecommerce.estoque.domain.events;

public record ItemPedidoEvent(
    String sku,
    Integer quantidade
) {}
