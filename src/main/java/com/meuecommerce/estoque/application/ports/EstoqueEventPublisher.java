package com.meuecommerce.estoque.application.ports;

public interface EstoqueEventPublisher {
    void estoqueReservado(
        Long pedidoId
    );

    void estoqueFalhaReserva(
        Long pedidoId
    );

    void estoqueLiberado(
        Long pedidoId
    );

    void estoqueBaixado(
        Long pedidoId,
        String sku,
        Integer quantidade
    );
}
