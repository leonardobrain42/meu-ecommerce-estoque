package com.meuecommerce.estoque.application.ports;

public interface EstoqueEventPublisher {
    void estoqueReservado(
        String pedidoId,
        String sku,
        Integer quantidade
    );

    void estoqueLiberado(
        String pedidoId,
        String sku,
        Integer quantidade
    );

    void estoqueBaixado(
        String pedidoId,
        String sku,
        Integer quantidade
    );
}
