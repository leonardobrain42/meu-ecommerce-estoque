package com.meuecommerce.estoque.domain.events;

import java.util.List;

public record PedidoCriadoEvent(
    Long pedidoId,
    List<ItemPedidoEvent> itens
) {}
